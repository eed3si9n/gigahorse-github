repatch-github
--------------

this is a dispatch plugin for github API v3.

## authentication

you can choose one from the four authenticating clients:

- Local Github Config (`LocalConfigClient`)
- OAuth (`OAuthClient`)
- BasicAuth (`BasicAuthClient`)
- none (`NoAuthClient`)

Local Github Config client uses OAuth token stored in the environment varable `GITHUB_TOKEN` or git config `github.token`.

### setting OAuth token to git config

create token:

1. [Account settings > Applications](https://github.com/settings/applications)
2. Personal access tokens > Generate new token


then save that token:

```
$ git config --global --add github.token your_token
```

now we can hit the GitHub API using the token:


```scala
scala> import dispatch._, Defaults._, repatch.github.request._
import dispatch._
import Defaults._
import repatch.github.request._

scala> val http = new Http
http: dispatch.Http = Http(com.ning.http.client.AsyncHttpClient@70e54515)

scala> val client = LocalConfigClient
client: repatch.github.request.LocalConfigClient.type = <function1>

scala> http(client(repo("dispatch", "reboot")) > as.json4s.Json)
res0: dispatch.Future[org.json4s.JValue] = scala.concurrent.impl.Promise$DefaultPromise@136e6221

scala> res0()
res1: org.json4s.JValue = JObject(List((id,JInt(2960515)), (name,JString(reboot)), (full_name,JString(dispatch/reboot))...
```

## general design

following the ethos of Dispatch Reboot, repatch-github splits request hander and response handler apart. the request handler is generally responsible for constructing the request URL sometimes with additional parameters or HTTP headers.

you can choose what format you would like Dispatch to return the response in. if you want raw string, you specify `as.String`. if you want json, you say `as.json4s.Json`. to assist the json parsing, repatch-github provides known field names as extractors under the companion object of the response classes.

if you just want a case classes of commonly used fields, you say `as.repatch.github.response.Repo` etc, and it would give you `Repo` case class.

## [repositories](https://developer.github.com/v3/repos/)

here's to querying for a repository as Json.

```scala
scala> val x = http(client(repo("dispatch", "reboot")) > as.json4s.Json)
x: dispatch.Future[org.json4s.JValue] = scala.concurrent.impl.Promise$DefaultPromise@2be9d442

scala> val json = x()
json: org.json4s.JValue = JObject(List((id,JInt(2960515)), (name,JString(reboot)), (full_name,JString(dispatch/reboot)), (owner,JObject(List((login,JString(dispatch)), (id,JInt(1115066)), ....

scala> {
         import repatch.github.response.Repo._
         import Owner._
         login(owner(json))
       }
res0: String = dispatch
```

here's the same query using case class response handler.


```scala
scala> val x = http(client(repo("dispatch", "reboot")) > as.repatch.github.response.Repo)
x: dispatch.Future[repatch.github.response.Repo] = scala.concurrent.impl.Promise$DefaultPromise@15447f19

scala> x()
res5: repatch.github.response.Repo = Repo(2960515,Owner(1115066,Organization,dispatch,https://avatars.githubusercontent.com/u/1115066?,c4050b114966f021d1d91d0b5baabd5c,...
```

## [references](https://developer.github.com/v3/git/refs/)

> This will return an array of all the references on the system, including things like notes and stashes if they exist on the server.

```scala
scala> val refs = http(client(repo("dispatch", "reboot").git_refs) > as.repatch.github.response.GitRefs)
refs: dispatch.Future[Seq[repatch.github.response.GitRef]] = scala.concurrent.impl.Promise$DefaultPromise@24a3332a

scala> refs()
res1: Seq[repatch.github.response.GitRef] = List(GitRef(refs/heads/0.9.x,https://api.github.com/repos/dispatch/reboot/git/refs/heads/0.9.x,GitObject(e547dd41a38e5d3c40576d00c929b25fc6d333a6,....
```

> The ref in the URL must be formatted as `heads/branch`, not just `branch`. 

```scala
scala> val ref = http(client(repo("dispatch", "reboot").git_refs.heads("master")) > as.repatch.github.response.GitRef)
ref: dispatch.Future[repatch.github.response.GitRef] = scala.concurrent.impl.Promise$DefaultPromise@7e955067

scala> ref()
res2: repatch.github.response.GitRef = GitRef(refs/heads/master,https://api.github.com/repos/dispatch/reboot/git/refs/heads/master,...
```

## [commits](https://developer.github.com/v3/git/commits/)

> `GET /repos/:owner/:repo/git/commits/:sha`

```scala
scala> val commit = http(client(repo("dispatch", "reboot").git_commit("bcf6d255317088ca1e32c6e6ecd4dce1979ac718")) > as.repatch.github.response.GitCommit)
commit: dispatch.Future[repatch.github.response.GitCommit] = scala.concurrent.impl.Promise$DefaultPromise@34110e6b

scala> commit()
res0: repatch.github.response.GitCommit = GitCommit(bcf6d255317088ca1e32c6e6ecd4dce1979ac718,https://api.github.com/repos/dispatch/reboot/git/commits/bcf6d255317088ca1e32c6e6ecd4dce1979ac718
```

git reference can also be passed in.

```scala
scala> val commit = for {
         master <- http(client(repo("dispatch", "reboot").git_refs.heads("master")) > as.repatch.github.response.GitRef)
         x      <- http(client(repo("dispatch", "reboot").git_commit(master)) > as.repatch.github.response.GitCommit)
       } yield x
commit: scala.concurrent.Future[repatch.github.response.GitCommit] = scala.concurrent.impl.Promise$DefaultPromise@39439e65

scala> commit()
res1: repatch.github.response.GitCommit = 
GitCommit(28dbd9265dd9780124c1412f7f530684dab020ae,https://api.github.com/repos/dispatch/reboot/git/commits/28dbd9265dd9780124c1412f7f530684dab020ae,
```

## [trees](https://developer.github.com/v3/git/trees/)

> `GET /repos/:owner/:repo/git/trees/:sha`

```scala
scala> val trees = http(client(repo("dispatch", "reboot").git_trees("b1193d20d761654b7fc35a48cd64b53aedc7a697")) > as.repatch.github.response.GitTrees)
trees: dispatch.Future[Seq[repatch.github.response.GitTree]] = scala.concurrent.impl.Promise$DefaultPromise@73259adf

scala> trees()
res8: Seq[repatch.github.response.GitTree] = List(GitTree(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,.gitignore,100644,blob,Some(93)), ...
```

> Get a tree recursively

```scala
scala> val trees = http(client(repo("dispatch", "reboot").git_trees("b1193d20d761654b7fc35a48cd64b53aedc7a697").recursive(10)) > as.repatch.github.response.GitTrees)
trees: dispatch.Future[Seq[repatch.github.response.GitTree]] = scala.concurrent.impl.Promise$DefaultPromise@b6c8874

scala> trees()
res9: Seq[repatch.github.response.GitTree] = List(GitTree(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,.gitignore,100644,blob,Some(93)), ...
```

here's how to get a tree from a git commit.

```scala
scala> val trees = for {
         master <- http(client(repo("dispatch", "reboot").git_refs.heads("master")) > as.repatch.github.response.GitRef)
         commit <- http(client(repo("dispatch", "reboot").git_commit(master)) > as.repatch.github.response.GitCommit)
         x      <- http(client(repo("dispatch", "reboot").git_trees(commit)) > as.repatch.github.response.GitTrees) 
       } yield x
trees: scala.concurrent.Future[Seq[repatch.github.response.GitTree]] = scala.concurrent.impl.Promise$DefaultPromise@3d333b3e

scala> trees()
res10: Seq[repatch.github.response.GitTree] = List(GitTree(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,.gitignore,100644,blob,Some(93)), ...
```

## [blobs](https://developer.github.com/v3/git/blobs/)

> `GET /repos/:owner/:repo/git/blobs/:sha`

```scala
scala> val blob = http(client(repo("dispatch", "reboot").git_blob(blob_sha)) > as.repatch.github.response.GitBlob)
blob: dispatch.Future[repatch.github.response.GitBlob] = scala.concurrent.impl.Promise$DefaultPromise@3943a8f9

scala> blob()
res11: repatch.github.response.GitBlob = 
GitBlob(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,base64,LmNsYXNzcGF0aAoucHJvamVjdAouc2V0dGluZ3MKdGFyZ2V0CnNjcmF0Y2gu
c2NhbGEKc3JjX21hbmFnZWQKKi50ZXN0LnByb3BlcnRpZXMKLmlkZWEKKi5p
bWwK
,93)

scala> res11.as_utf8
res12: String = 
".classpath
.project
.settings
target
scratch.scala
src_managed
*.test.properties
.idea
*.iml
"
```

git the blob as raw.

```scala
scala> http(client(repo("dispatch", "reboot").git_blob(blob_sha).raw) > as.String)
res13: dispatch.Future[String] = scala.concurrent.impl.Promise$DefaultPromise@522c7ab9

scala> res13()
res14: String = 
".classpath
.project
.settings
target
scratch.scala
src_managed
*.test.properties
.idea
*.iml
"
```
