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
scala> import dispatch._, Defaults._, repatch.github.{request => gh}
import dispatch._
import Defaults._
import repatch.github.{request => gh}

scala> val http = new Http
http: dispatch.Http = Http(com.ning.http.client.AsyncHttpClient@70e54515)

scala> val client = gh.LocalConfigClient()
client: repatch.github.request.LocalConfigClient = LocalConfigClient(OAuthClient(xxxxxxxx,List(StringMediaType(application/json), GithubMediaType(Some(v3),None,Some(json)))))

scala> http(client(gh.repo("dispatch", "reboot")) > as.json4s.Json)
res0: dispatch.Future[org.json4s.JValue] = scala.concurrent.impl.Promise$DefaultPromise@136e6221

scala> res0()
res1: org.json4s.JValue = JObject(List((id,JInt(2960515)), (name,JString(reboot)), (full_name,JString(dispatch/reboot))...
```

## general design

following the ethos of Dispatch Reboot, repatch-github splits request hander and response handler apart. the request handler is generally responsible for constructing the request URL sometimes with additional parameters or HTTP headers.

you can choose what format you would like Dispatch to return the response in. if you want raw string, you specify `as.String`. if you want json, you say `as.json4s.Json`. to assist the json parsing, repatch-github provides known field names as extractors under the companion object of the response classes.

if you just want a case classes of commonly used fields, you say `as.repatch.github.response.Repo` etc, and it would give you `Repo` case class.

### media type

> Custom media types are used in the API to let consumers choose the format of the data they wish to receive. This is done by adding one or more of the following types to the `Accept` header when you make a request.

media type variations are supported on authenticating clients by mixing in `Mime[R]`. Here's an example of `raw` format:

```scala
scala> http(client.raw(gh.repo("dispatch", "reboot").git_blob(blob_sha)) > as.String)
res1: dispatch.Future[String] = scala.concurrent.impl.Promise$DefaultPromise@60c821c4
```

### pagination

> Requests that return multiple items will be paginated to 30 items by default. 
>
> The pagination info is included in the Link header. It is important to follow these Link header values instead of constructing your own URLs.

paged responses can be parsed into `Paged[A]`, which wraps `Seq[A]` url links included in HTTP header, and optionally total count for search result.

```scala
scala> val iss = http(client(gh.repo("dispatch", "reboot").issues.page(1).per_page(1)) > as.repatch.github.response.Issues)
iss: dispatch.Future[repatch.github.response.Paged[repatch.github.response.Issue]] = scala.concurrent.impl.Promise$DefaultPromise@442e7590

scala> iss().next_page
res1: Option[String] = Some(https://api.github.com/repositories/2960515/issues?page=2&per_page=1)

scala> val iss2 = http(client(gh.url(iss().next_page.get)) > as.repatch.github.response.Issues)
iss2: dispatch.Future[repatch.github.response.Paged[repatch.github.response.Issue]] = scala.concurrent.impl.Promise$DefaultPromise@7c0d3bdb

scala> iss2()
res2: repatch.github.response.Paged[repatch.github.response.Issue] = 
Paged(List(Issue(https://api.github.com/repos/dispatch/reboot/issues/75,Some(https://github.com/dispatch/reboot/issues/75),...
```

## [repositories](https://developer.github.com/v3/repos/)

here's to querying for a repository as Json.

```scala
scala> val x = http(client(gh.repo("dispatch", "reboot")) > as.json4s.Json)
x: dispatch.Future[org.json4s.JValue] = scala.concurrent.impl.Promise$DefaultPromise@2be9d442

scala> val json = x()
json: org.json4s.JValue = JObject(List((id,JInt(2960515)), (name,JString(reboot)), (full_name,JString(dispatch/reboot)), (owner,JObject(List((login,JString(dispatch)), (id,JInt(1115066)), ....

scala> {
         import repatch.github.response.Repo._
         import repatch.github.response.User._
         login(owner(json))
       }
res0: String = dispatch
```

here's the same query using case class response handler.


```scala
scala> val x = http(client(gh.repo("dispatch", "reboot")) > as.repatch.github.response.Repo)
x: dispatch.Future[repatch.github.response.Repo] = scala.concurrent.impl.Promise$DefaultPromise@15447f19

scala> x()
res5: repatch.github.response.Repo = Repo(2960515,User(1115066,Organization,dispatch,https://avatars.githubusercontent.com/u/1115066?,c4050b114966f021d1d91d0b5baabd5c,...
```

## [references](https://developer.github.com/v3/git/refs/)

> This will return an array of all the references on the system, including things like notes and stashes if they exist on the server.

```scala
scala> val refs = http(client(gh.repo("dispatch", "reboot").git_refs) > as.repatch.github.response.GitRefs)
refs: dispatch.Future[repatch.github.response.Paged[repatch.github.response.GitRef]] = scala.concurrent.impl.Promise$DefaultPromise@6ea6ba8d

scala> refs()
res3: repatch.github.response.Paged[repatch.github.response.GitRef] = Paged(List(GitRef(refs/heads/0.9.x,https://api.github.com/repos/dispatch/reboot/git/refs/heads/0.9.x,GitObject(e547dd41a38e5d3c40576d00c929b25fc6d333a6...
```

> The ref in the URL must be formatted as `heads/branch`, not just `branch`. 

```scala
scala> val ref = http(client(gh.repo("dispatch", "reboot").git_refs.heads("master")) > as.repatch.github.response.GitRef)
ref: dispatch.Future[repatch.github.response.GitRef] = scala.concurrent.impl.Promise$DefaultPromise@7e955067...

scala> ref()
res2: repatch.github.response.GitRef = GitRef(refs/heads/master,https://api.github.com/repos/dispatch/reboot/git/refs/heads/master,...
```

> You can also request a sub-namespace. For example, to get all the tag references, you can call:
>
> `GET /repos/:owner/:repo/git/refs/tags`

```scala
scala> val tagRefs = http(client(gh.repo("dispatch", "reboot").git_refs.tags) > as.repatch.github.response.GitRefs)
tagRefs: dispatch.Future[repatch.github.response.Paged[repatch.github.response.GitRef]] = scala.concurrent.impl.Promise$DefaultPromise@736577b2

scala> tagRefs()
res4: repatch.github.response.Paged[repatch.github.response.GitRef] = Paged(List(GitRef(refs/tags/0.9.0,https://api.github.com/repos/dispatch/reboot/git/refs/tags/0.9.0,GitObject(b2097a582b7763c1c1a44b9ead0123ba10dbb273,...
```

## [commits](https://developer.github.com/v3/git/commits/)

> `GET /repos/:owner/:repo/git/commits/:sha`

```scala
scala> val commit = http(client(gh.repo("dispatch", "reboot").git_commit("bcf6d255317088ca1e32c6e6ecd4dce1979ac718")) > as.repatch.github.response.GitCommit)
commit: dispatch.Future[repatch.github.response.GitCommit] = scala.concurrent.impl.Promise$DefaultPromise@34110e6b

scala> commit()
res0: repatch.github.response.GitCommit = GitCommit(bcf6d255317088ca1e32c6e6ecd4dce1979ac718,https://api.github.com/repos/dispatch/reboot/git/commits/bcf6d255317088ca1e32c6e6ecd4dce1979ac718...
```

git reference can also be passed in.

```scala
scala> val commit = for {
         master <- http(client(gh.repo("dispatch", "reboot").git_refs.heads("master")) > as.repatch.github.response.GitRef)
         x      <- http(client(gh.repo("dispatch", "reboot").git_commit(master)) > as.repatch.github.response.GitCommit)
       } yield x
commit: scala.concurrent.Future[repatch.github.response.GitCommit] = scala.concurrent.impl.Promise$DefaultPromise@39439e65

scala> commit()
res1: repatch.github.response.GitCommit = 
GitCommit(28dbd9265dd9780124c1412f7f530684dab020ae,https://api.github.com/repos/dispatch/reboot/git/commits/28dbd9265dd9780124c1412f7f530684dab020ae,...
```

## [trees](https://developer.github.com/v3/git/trees/)

> `GET /repos/:owner/:repo/git/trees/:sha`

```scala
scala> val trees = http(client(gh.repo("dispatch", "reboot").git_trees("b1193d20d761654b7fc35a48cd64b53aedc7a697")) > as.repatch.github.response.GitTrees)
trees: dispatch.Future[repatch.github.response.GitTrees] = scala.concurrent.impl.Promise$DefaultPromise@2e39619b

scala> trees()
res5: repatch.github.response.GitTrees = GitTrees(b1193d20d761654b7fc35a48cd64b53aedc7a697,https://api.github.com/repos/dispatch/reboot/git/trees/b1193d20d761654b7fc35a48cd64b53aedc7a697,List(GitTree(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,.gitignore,100644,blob,Some(93)), ...
```

> Get a tree recursively

```scala
scala> val trees = http(client(gh.repo("dispatch", "reboot").git_trees("b1193d20d761654b7fc35a48cd64b53aedc7a697").recursive(10)) > as.repatch.github.response.GitTrees)
trees: dispatch.Future[repatch.github.response.GitTrees] = scala.concurrent.impl.Promise$DefaultPromise@1a3fb85d

scala> trees()
res7: repatch.github.response.GitTrees = GitTrees(b1193d20d761654b7fc35a48cd64b53aedc7a697,https://api.github.com/repos/dispatch/reboot/git/trees/b1193d20d761654b7fc35a48cd64b53aedc7a697,List(GitTree(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,.gitignore,100644,blob,Some(93)),...
```

here's how to get a tree from a git commit.

```scala
scala> val trees = for {
         master <- http(client(gh.repo("dispatch", "reboot").git_refs.heads("master")) > as.repatch.github.response.GitRef)
         commit <- http(client(gh.repo("dispatch", "reboot").git_commit(master)) > as.repatch.github.response.GitCommit)
         x      <- http(client(gh.repo("dispatch", "reboot").git_trees(commit)) > as.repatch.github.response.GitTrees) 
       } yield x
trees: scala.concurrent.Future[repatch.github.response.GitTrees] = scala.concurrent.impl.Promise$DefaultPromise@2cbb346d

scala> trees()
res8: repatch.github.response.GitTrees = GitTrees(4972c45a48bbb73f0f110e58eae6d13ff6349566,https://api.github.com/repos/dispatch/reboot/git/trees/4972c45a48bbb73f0f110e58eae6d13ff6349566,List(GitTree(3baebe52555bc73ad1c9a94261c4552fb8d771cd,https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd,.gitignore,100644,blob,Some(93)), ...
```

## [blobs](https://developer.github.com/v3/git/blobs/)

> `GET /repos/:owner/:repo/git/blobs/:sha`

```scala
scala> val blob_sha = "3baebe52555bc73ad1c9a94261c4552fb8d771cd"
blob_sha: String = 3baebe52555bc73ad1c9a94261c4552fb8d771cd

scala> val blob = http(client(gh.repo("dispatch", "reboot").git_blob(blob_sha)) > as.repatch.github.response.GitBlob)
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
scala> http(client.raw(gh.repo("dispatch", "reboot").git_blob(blob_sha)) > as.String)
res1: dispatch.Future[String] = scala.concurrent.impl.Promise$DefaultPromise@60c821c4

scala> res1()
res2: String = 
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

## [issues](https://developer.github.com/v3/issues/)

> List all issues across all the authenticated user’s visible repositories including owned repositories, member repositories, and organization repositories:
>
> `GET /issues`

```scala
scala> val iss = http(client(gh.issues) > as.repatch.github.response.Issues)
iss: dispatch.Future[repatch.github.response.Paged[repatch.github.response.Issue]] = scala.concurrent.impl.Promise$DefaultPromise@45b81f84

scala> iss()
res9: repatch.github.response.Paged[repatch.github.response.Issue] = 
Paged(List(Issue(https://api.github.com/repos/sbt/sbt/issues/1149,Some(https://github.com/sbt/sbt/issues/1149),Some(1149),Some(open),Some(Documentation for AutoPlugins),S...
```

parameters can be passed in as chained method calls.

```scala
scala> val iss = http(client(gh.issues.labels(Seq("bug")).direction("asc")) > as.repatch.github.response.Issues)
iss: dispatch.Future[repatch.github.response.Paged[repatch.github.response.Issue]] = scala.concurrent.impl.Promise$DefaultPromise@d30bfb7

scala> iss()
res11: repatch.github.response.Paged[repatch.github.response.Issue] = 
Paged(List(Issue(https://api.github.com/repos/eed3si9n/scalaxb/issues/232,Some(https://github.com/eed3si9n/scalaxb/issues/232),...
```

## [search](https://developer.github.com/v3/search/)

### search repositories

> Find repositories via various criteria. This method returns up to 100 results per page.

here's how to run search for repositories. note response hander needs to be `ReposSearch` instead of `Repos`.

```scala
scala> val repos = http(client(gh.search.repos("reboot language:scala")) > as.repatch.github.response.ReposSearch)
repos: dispatch.Future[repatch.github.response.Paged[repatch.github.response.Repo]] = scala.concurrent.impl.Promise$DefaultPromise@38b556d2

scala> repos()
res0: repatch.github.response.Paged[repatch.github.response.Repo] = Paged(List(Repo(2960515,User(https://api.github.com/users/dispatch,dispatch,1115066,https://github.com/dispatch,https://avatars.githubusercontent.com/u/1115066?,Some(c4050b114966f021d1d91d0b5baabd5c),Organization,false,None,None),reboot,dispatch/reboot,Some(Dispatch with AsyncHttpClient as the underlying library),false,false,https://api.github.com/repos/dispatch/reboot,https://github.com/dispatch/reboot,https://github.com/dispatch/reboot.git,git://github.com/dispatch/reboot.git,git@github.com:dispatch/reboot.git,Some(),Some(Scala),57,188,1012,master,16,Some(java.util.GregorianCalendar[time=?,areFieldsSet=false,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id="GMT+00:00",offset=0,dstSavings=0,useDayli...
scala> repos().total_count_opt
res1: Option[BigInt] = Some(7)
```
