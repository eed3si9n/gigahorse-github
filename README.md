repatch-github
--------------

this is a dispatch plugin for github API v3.

## setup

### using source deps

use source dependency to point to a commit hash of this project.

```scala
import sbt._, Keys._

object build extends Build{
  lazy val root = Project(
    "project_name",
    file(".")
  ) settings (
    scalaVersion := "2.9.2"
  ) dependsOn (
    uri("git://github.com/eed3si9n/repatch-github#hash")
  )
}
```

## authentication

you can choose one from the four authenticating clients:

- automatic (`Client`)
- OAuth (`OAuthClient`)
- BasicAuth (`BasicAuthClient`)
- none (`NoAuthClient`)

the automatic client uses OAuth token stored in the environment varable `GITHUB_TOKEN` or git config `github.token`.

### setting OAuth token to git config

create token:

```
$ curl -u 'username' -d '{"scopes":[]}' https://api.github.com/authorizations
Enter host password for user 'username': [type password]
{
  "app": {
    "url": "http://developer.github.com/v3/oauth/#oauth-authorizations-api",
    "name": "GitHub API"
  },
  "token": "your_token",
  "updated_at": "2012-12-08T22:13:41Z",
  "url": "https://api.github.com/authorizations/123456",
  "note_url": null,  
  "scopes": [
  ],
  "note": null,
  "created_at": "2012-12-08T22:13:41Z",
  "id": 123456,
}
```

then save that token:

```
$ git config --global --add github.token your_token
```

## request builders and response handlers

repatch-github provides two categories of classes: request builders and response handlers.

request builders, when used in conjunction with authenticating client, build a `dispatch.Req` object. to distinguish from response case classes, these classes ends with `s` like `Repos`.

response handlers helps you parse the response from github.

### repos API using case class handler

```scala
scala> import dispatch._, repatch._, repatch.github._
import dispatch._
import repatch._
import repatch.github._

scala> Http(Client(Repos("dispatch", "dispatch")) > as_github.Repo)
res0: dispatch.Promise[dispatch.github.Repo] = Promise(-incomplete-)

scala> res0()
res1: repatch.github.Repo = Repo(408372,Owner(1115066,Organization,dispatch,...
```

### repos API using json handler

```scala
scala> Http(Client(Repos("dispatch", "dispatch")) > as_github.Json)
res2: dispatch.Promise[net.liftweb.json.package.JValue] = Promise(-incomplete-)

scala> res2 map { js =>
         import Repo._
         full_name(js)
       }
res3: dispatch.Promise[Option[String]] = Promise(Some(dispatch/dispatch))

scala> res3().head
res4: String = dispatch/dispatch
```

### git blob API using string hander

```scala
scala> val blob_sha = "fb4c8b459f05bcc5296d9c13a3f6757597786f1d"
blob_sha: java.lang.String = fb4c8b459f05bcc5296d9c13a3f6757597786f1d

scala> Http(Client(Repos("dispatch", "dispatch").git_blob(blob_sha).raw) > as.String)
res5: dispatch.Promise[String] = Promise(-incomplete-)

scala> res5()
res6: String = 
".classpath
.project
.settings
target
scratch.scala
lib_managed
project/boot
project/plugins/project
src_managed
*.test.properties
"
```
