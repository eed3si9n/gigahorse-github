gigahorse-github
----------------

gigahorse-github is a Gigahorse plugin for github API v3.

## setup

```scala
libraryDependencies ++= List"com.eed3si9n" %% "gigahorse-github" % "gigahorse0.3.1_0.2.0", "com.eed3si9n" %% "gigahorse-okhttp" % "0.3.1")
```

## authentication

you can choose one from the four authenticating clients:

- Local Github Config (`Github.localConfigClient`)
- OAuth (`Github.oauthClient`)
- BasicAuth (`Github.basicAuthClient`)
- none (`Github.noAuthClient`)

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
scala> import gigahorse._, support.okhttp.Gigahorse
import gigahorse._

scala> import gigahorse.github.Github
import gigahorse.github.Github

scala> import scala.concurrent._, duration._
import scala.concurrent._
import duration._

scala> val client = Github.localConfigClient
client: gigahorse.github.LocalConfigClient = LocalConfigClient(OAuthClient(****, List(StringMediaType(application/json), GithubMediaType(Some(v3),None,Some(json)))))

scala> Gigahorse.withHttp { http =>
         val f = http.run(client(Github.repo("eed3si9n", "gigahorse")), Github.asRepo)
         Await.result(f, 2.minutes)
       }
res0: gigahorse.github.response.Repo = Repo(https://api.github.com/repos/eed3si9n/gigahorse, gigahorse, 64110679, User(https://api.github.com/users/eed3si9n, eed3si9n, 184683, Some(https://github.com/eed3si9n), Some(https://avatars.githubusercontent.com/u/184683?v=3), Some(), Some(User), Some(true), None, None), eed3si9n/gigahorse, Some(an HTTP client for Scala with Async Http Client underneath), true, true, Some(https://github.com/eed3si9n/gigahorse), Some(https://github.com/eed3si9n/gigahorse.git), Some(git://github.com/eed3si9n/gigahorse.git), Some(git@github.com:eed3si9n/gigahorse.git), Some(http://eed3si9n.com/gigahorse), Some(Scala), Some(0), Some(23), Some(187), Some(0.1.x), Some(0), Some(java.util.GregorianCalendar[time=?,areFieldsSet=false,areAllFieldsSet=true,lenient=true,zone...
```

## general design

Following the ethos of Gigahorse (and Dispatch Reboot), gigahorse-github splits request hander and response handler apart. the request handler is generally responsible for constructing the request URL sometimes with additional parameters or HTTP headers.

You can choose what format you would like Gigahorse to return the response in. If you want raw string, you specify `Github.asString`. if you want JSON, you say `Github.asJson`.

If you just want case classes of commonly used fields, you say `Github.asRepo` etc, and it would give you `Repo` case class.

### media type

> Custom media types are used in the API to let consumers choose the format of the data they wish to receive. This is done by adding one or more of the following types to the `Accept` header when you make a request.

media type variations are supported on authenticating clients by mixing in `Mime[R]`. Here's an example of `raw` format:

```scala
scala> val blob_sha = "ac28ec8ee30e89ae807f3ef52f471ffc68783b28"
blob_sha: String = ac28ec8ee30e89ae807f3ef52f471ffc68783b28

scala> Gigahorse.withHttp { http =>
         val f = http.run(client.raw(Github.repo("eed3si9n", "gigahorse").git_blob(blob_sha)), Github.asString)
         Await.result(f, 2.minutes)
       }
res1: String =
"project/pf.sbt
pf.sbt

rsync.sh
"
```

### pagination

> Requests that return multiple items will be paginated to 30 items by default. 
>
> The pagination info is included in the Link header. It is important to follow these Link header values instead of constructing your own URLs.

paged responses can be parsed into `Paged[A]`, which wraps `Seq[A]` url links included in HTTP header, and optionally total count for search result.

```scala
scala> val http = Gigahorse.http(Gigahorse.config)
http: gigahorse.HttpClient = AchHttpClient(com.ning.http.client.AsyncHttpClientConfig@6c8de61d)

scala> val f = http.run(client(Github.repo("sbt", "sbt").issues.page(1).per_page(1)), Github.asIssues)
f: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.Issue]] = List()

scala> val iss = Await.result(f, 2.minutes)
iss: gigahorse.github.response.Paged[gigahorse.github.response.Issue] =
Paged(Vector(Issue(https://api.github.com/repos/sbt/sbt/issues/2686, Some(https://github.com/sbt/sbt/issues/2686), Some(2686), Some(open), Some(updateSbtClassifiers ...

scala> iss.next_page
res2: Option[String] = Some(https://api.github.com/repositories/279553/issues?page=2&per_page=1)

scala> val f2 = http.run(client(Github.url(iss.next_page.get)), Github.asIssues)
f2: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.Issue]] = List()

scala> val iss2 = Await.result(f2, 2.minutes)
iss2: gigahorse.github.response.Paged[gigahorse.github.response.Issue] =
Paged(Vector(Issue(https://api.github.com/repos/sbt/sbt/issues/2685, Some(https://github.com/sbt/sbt/issues/2685), Some(2685), Some(open), Some(scripted ...

scala> http.close // make sure you call this
```

## [repositories](https://developer.github.com/v3/repos/)

here's to querying for a repository as Json.

```scala
scala> Gigahorse.withHttp(Gigahorse.config) { http =>
         val f = http.run(client(Github.repo("eed3si9n", "gigahorse")), Github.asJson)
         Await.result(f, 2.minutes)
       }
res5: scala.json.ast.unsafe.JValue = JObject([Lscala.json.ast.unsafe.JField;@2daef479)
scala> val json = x()
json: org.json4s.JValue = JObject(List((id,JInt(2960515)), (name,JString(reboot)), (full_name,JString(dispatch/reboot)), (owner,JObject(List((login,JString(dispatch)), (id,JInt(1115066)), ....

scala> CompactPrinter(res5)
res6: String = {"default_branch":"0.1.x","stargazers_url":"https://api.github.com/repos/eed3si9n/gigahorse/stargazers",....
```

here's the same query using case class response handler.

```scala
scala> val repo = Gigahorse.withHttp(Gigahorse.config) { http =>
         val f = http.run(client(Github.repo("eed3si9n", "gigahorse")), Github.asRepo)
         Await.result(f, 2.minutes)
       }
repo: gigahorse.github.response.Repo = Repo(https://api.github.com/repos/eed3si9n/gigahorse, gigahorse, 64110679,....
```

> List repositories for the authenticated user.

here's how to list repositories for the authenticated user. optionally, `sort` parameter can be passed in.

```scala
scala> val repos = Gigahorse.withHttp(Gigahorse.config) { http =>
         val f = http.run(client(Github.user.repos.sort("pushed").asc), Github.asRepos)
         Await.result(f, 2.minutes)
       }
repos: gigahorse.github.response.Paged[gigahorse.github.response.Repo] = Paged(Vector(Repo(https://api.github.com/repos/eed3si9n/eed3si9n.github.com...
```

here's how to do the same for a specific user.

```scala
scala> val repos = Gigahorse.withHttp(Gigahorse.config) { http =>
         val f = http.run(client(Github.user("eed3si9n").repos.sort("pushed").asc), Github.asRepos)
         Await.result(f, 2.minutes)
       }
repos: gigahorse.github.response.Paged[gigahorse.github.response.Repo] = Paged(Vector(Repo(https://api.github.com/repos/eed3si9n/eed3si9n.github.com...
```

## [references](https://developer.github.com/v3/git/refs/)

> This will return an array of all the references on the system, including things like notes and stashes if they exist on the server.

```scala
scala> val http = Gigahorse.http(Gigahorse.config)
http: gigahorse.HttpClient = AchHttpClient(com.ning.http.client.AsyncHttpClientConfig@416e404f)

scala> val refs = http.run(client(Github.repo("eed3si9n", "gigahorse").git_refs), Github.asGitRefs)
refs: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.GitRef]] = List()

scala> Await.result(refs, 2.minutes)
res9: gigahorse.github.response.Paged[gigahorse.github.response.GitRef] = Paged(Vector(GitRef(....
```

> The ref in the URL must be formatted as `heads/branch`, not just `branch`.

```scala
scala> val ref = http.run(client(Github.repo("eed3si9n", "gigahorse").git_refs.heads("0.1.x")), Github.asGitRef)
ref: scala.concurrent.Future[gigahorse.github.response.GitRef] = List()

scala> Await.result(refs, 2.minutes)
res10: gigahorse.github.response.Paged[gigahorse.github.response.GitRef] = Paged(Vector(GitRef(....
```

> You can also request a sub-namespace. For example, to get all the tag references, you can call:
>
> `GET /repos/:owner/:repo/git/refs/tags`


val tagRefs = http.run(client(Github.repo("eed3si9n", "gigahorse").git_refs.tags), Github.asGitRefs)

```scala
scala> val tagRefs = http.run(client(Github.repo("eed3si9n", "gigahorse").git_refs.tags), Github.asGitRefs)
tagRefs: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.GitRef]] = List()

scala> Await.result(tagRefs, 2.minutes)
res11: gigahorse.github.response.Paged[gigahorse.github.response.GitRef] = Paged(Vector(GitRef(https://api.github.com/repos/eed3si9n/gigahorse/git/refs/tags/v0.1.0...

scala> http.close // make sure you call this
```

## [commits](https://developer.github.com/v3/git/commits/)

> `GET /repos/:owner/:repo/git/commits/:sha`

```scala
scala> val commit_sha = "d7b8bb43d003e58f55af7b3592e7ce1fb986d0f3"
commit_sha: String = d7b8bb43d003e58f55af7b3592e7ce1fb986d0f3

scala> val commit = Gigahorse.withHttp(Gigahorse.config) { http =>
         val f = http.run(client(Github.repo("eed3si9n", "gigahorse").git_commit(commit_sha)), Github.asGitCommit)
         Await.result(f, 2.minutes)
       }
commit: gigahorse.github.response.GitCommit = GitCommit(https://api.github.com/repos/eed3si9n/gigahorse/git/commits/d7b8bb43d003e58f55af7b3592e7ce1fb986d0f3,....
```

git reference can also be passed in.

```scala
scala> import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.ExecutionContext.Implicits._

scala> val commit = Gigahorse.withHttp(Gigahorse.config) { http =>
         val f = for {
           // this returns a GitRef case class
           master <- http.run(client(Github.repo("eed3si9n", "gigahorse").git_refs.heads("0.1.x")), Github.asGitRef)
           // this returns a GitCommit case class
           x <- http.run(client(Github.repo("eed3si9n", "gigahorse").git_commit(master)), Github.asGitCommit)
         } yield x
         Await.result(f, 2.minutes)
       }
GitCommit(https://api.github.com/repos/eed3si9n/gigahorse/git/commits/a68e16e6e572241d0fa19c94e4d393d80362fa4b...
```

## [trees](https://developer.github.com/v3/git/trees/)

> `GET /repos/:owner/:repo/git/trees/:sha`

```scala
scala> val http = Gigahorse.http(Gigahorse.config)
http: gigahorse.HttpClient = AchHttpClient(com.ning.http.client.AsyncHttpClientConfig@1c5e0a3)

scala> val tree_sha = "d19f416669ea6a2ffc22ab91bed8a9feff48e778"
tree_sha: String = d19f416669ea6a2ffc22ab91bed8a9feff48e778

scala> val trees = http.run(client(Github.repo("eed3si9n", "gigahorse").git_trees(tree_sha)), Github.asGitTrees)
trees: scala.concurrent.Future[gigahorse.github.response.GitTrees] = List()

scala> Await.result(trees, 2.minutes)
res13: gigahorse.github.response.GitTrees = GitTrees(https://api.github.com/repos/eed3si9n/gigahorse/git/trees/d19f416669ea6a2ffc22ab91bed8a9feff48e778, ...
```

> Get a tree recursively

```scala
scala> val trees = http.run(client(Github.repo("eed3si9n", "gigahorse").git_trees(tree_sha).recursive(10)), Github.asGitTrees)
trees: scala.concurrent.Future[gigahorse.github.response.GitTrees] = List()

scala> Await.result(trees, 2.minutes)
res14: gigahorse.github.response.GitTrees = GitTrees(https://api.github.com/repos/eed3si9n/gigahorse/git/trees/d19f416669ea6a2ffc22ab91bed8a9feff48e778,,...
```

here's how to get a tree from a git commit.

```scala
scala> val trees = for {
         commit <- http.run(client(Github.repo("eed3si9n", "gigahorse").git_commit(commit_sha)), Github.asGitCommit)
         trees <- http.run(client(Github.repo("eed3si9n", "gigahorse").git_trees(commit)), Github.asGitTrees)
       } yield trees
trees: scala.concurrent.Future[gigahorse.github.response.GitTrees] = List()

scala> Await.result(trees, 2.minutes)
res15: gigahorse.github.response.GitTrees = GitTrees(https://api.github.com/repos/eed3si9n/gigahorse/git/trees/d19f416669ea6a2ffc22ab91bed8a9feff48e778, ...

scala> http.close // make sure you call this
```

## [blobs](https://developer.github.com/v3/git/blobs/)

> `GET /repos/:owner/:repo/git/blobs/:sha`

```scala
scala> val http = Gigahorse.http(Gigahorse.config)
http: gigahorse.HttpClient = AchHttpClient(com.ning.http.client.AsyncHttpClientConfig@1c5e0a3)

scala> val blob_sha = "ac28ec8ee30e89ae807f3ef52f471ffc68783b28"
blob_sha: String = ac28ec8ee30e89ae807f3ef52f471ffc68783b28

scala> val blob = http.run(client(Github.repo("eed3si9n", "gigahorse").git_blob(blob_sha)), Github.asGitBlob)
blob: scala.concurrent.Future[gigahorse.github.response.GitBlob] = List()

scala> Await.result(blob, 2.minutes)
res20: gigahorse.github.response.GitBlob =
GitBlob(https://api.github.com/repos/eed3si9n/gigahorse/git/blobs/ac28ec8ee30e89ae807f3ef52f471ffc68783b28, ac28ec8ee30e89ae807f3ef52f471ffc68783b28, base64, cHJvamVjdC9wZi5zYnQKcGYuc2J0Cgpyc3luYy5zaAo=
, Some(32))
```

git the blob as raw. (Note `client.raw`)

```scala
scala> val blob = http.run(client.raw(Github.repo("eed3si9n", "gigahorse").git_blob(blob_sha)), Github.asString)
blob: scala.concurrent.Future[String] = List()

scala> Await.result(blob, 2.minutes)
res21: String =
"project/pf.sbt
pf.sbt

rsync.sh
"

scala> http.close // make sure you call this
```

## [issues](https://developer.github.com/v3/issues/)

> List all issues across all the authenticated user’s visible repositories including owned repositories, member repositories, and organization repositories:
>
> `GET /issues`

```scala
scala> val http = Gigahorse.http(Gigahorse.config)
http: gigahorse.HttpClient = AchHttpClient(com.ning.http.client.AsyncHttpClientConfig@1c5e0a3)

scala> val iss = http.run(client(Github.issues), Github.asIssues)
iss: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.Issue]] = List()

scala> Await.result(iss, 2.minutes)
res23: gigahorse.github.response.Paged[gigahorse.github.response.Issue] =
Paged(Vector(Issue(https://api.github.com/repos/sbt/sbt/issues/2573, Some(https://github.com/sbt/sbt/issues/2573), ....
```

parameters can be passed in as chained method calls.

```scala
scala> {
         val iss = http.run(client(Github.issues.state(Github.IssueState.closed).labels("bug").asc), Github.asIssues)
         Await.result(iss, 2.minutes)
       }
res26: gigahorse.github.response.Paged[gigahorse.github.response.Issue] =
Paged(Vector(Issue(https://api.github.com/repos/eed3si9n/scalaxb/issues/34...

scala> http.close // make sure you call this
```

## [users](https://developer.github.com/v3/users/)

get a single user.

```scala
scala> val http = Gigahorse.http(Gigahorse.config)
http: gigahorse.HttpClient = AchHttpClient(com.ning.http.client.AsyncHttpClientConfig@1c5e0a3)

scala> val usr = http.run(client(Github.user("eed3si9n")), Github.asUser)
usr: scala.concurrent.Future[gigahorse.github.response.User] = List()

scala> Await.result(usr, 2.minutes)
res27: gigahorse.github.response.User = User(https://api.github.com/users/eed3si9n, eed3si9n, 184683, Some(https://github.com/eed3si9n),...
```

get the authenticated user.

```scala
scala> val usr = http.run(client(Github.user), Github.asUser)
usr: scala.concurrent.Future[gigahorse.github.response.User] = List()

scala> Await.result(usr, 2.minutes)
res28: gigahorse.github.response.User = User(https://api.github.com/users/eed3si9n, eed3si9n, 184683, Some(https://github.com/eed3si9n),...
```

## [organizations](https://developer.github.com/v3/orgs/)

> List all public organizations for an unauthenticated user. Lists private and public organizations for authenticated users.

```scala
scala> val orgs = http.run(client(Github.user("eed3si9n").orgs), Github.asOrgs)
orgs: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.User]] = List()

scala> Await.result(orgs, 2.minutes)
res29: gigahorse.github.response.Paged[gigahorse.github.response.User] = Paged(Vector(User(https://api.github.com/orgs/scala, scala, 57059, None, Some(https://avatars.githubusercontent.com/u/57059?v=3),...
```

## [search](https://developer.github.com/v3/search/)

### search repositories

> Find repositories via various criteria. This method returns up to 100 results per page.

here's how to run search for repositories. note response hander needs to be `ReposSearch` instead of `Repos`.

```scala
scala> val repos = http.run(client(Github.search.repos("gigahorse language:scala")), Github.asReposSearch)
repos: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.Repo]] = List()

scala> Await.result(repos, 2.minutes)
res30: gigahorse.github.response.Paged[gigahorse.github.response.Repo] = Paged(Vector(Repo(https://api.github.com/repos/eed3si9n/gigahorse, gigahorse, 64110679,

scala> Await.result(repos, 2.minutes).total_count
res31: Option[Long] = Some(2)
```

### search code

> Find file contents via various criteria. (This method returns up to 100 results per page.)

use `CodeSearch` as the response handler. this returns `Paged[BlobRef]`.

```scala
scala> val code = http.run(client(Github.search.code("\"abstract class Gigahorse\" in:file repo:eed3si9n/gigahorse")),
         Github.asCodeSearch)
code: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.BlobRef]] = List()

scala> Await.result(code, 2.minutes)
res32: gigahorse.github.response.Paged[gigahorse.github.response.BlobRef] = Paged(Vector(BlobRef(https://api.github.com/repositories/64110679/contents/core/src/main/scala/gigahorse/Gigahorse.scala?ref=2c795c967c43cd5ab0782c2a4a86cd5413d5536a,...
```

searching also supports `text_match` media type, which uses `TextMatches` response handler.

```scala
scala> val tms = http.run(client.text_match(Github.search.code("\"abstract class Gigahorse\" in:file repo:eed3si9n/gigahorse")),
         Github.asTextMatches)
tms: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.TextMatches]] = List()

scala> Await.result(tms, 2.minutes)
res33: gigahorse.github.response.Paged[gigahorse.github.response.TextMatches] =
Paged(Vector(TextMatches(Vector(TextMatch(https://api.github.com/repositories/64110679/contents/core/src/main/scala/gigahorse/Gigahorse.scala?ref=2c795c967c43cd5ab0782c2a4a86cd5413d5536a, FileContent....
```

### search issues

> Find issues by state and keyword. (This method returns up to 100 results per page.)

use `ReposSearch` as the response handler.

```scala
scala> val iss = http.run(client(Github.search.issues("sbt-datatype 0.2.3 repo:eed3si9n/gigahorse")), Github.asIssuesSearch)
iss: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.Issue]] = List()

scala> Await.result(iss, 2.minutes)
res34: gigahorse.github.response.Paged[gigahorse.github.response.Issue] = Paged(Vector(Issue(https://api.github.com/repos/eed3si9n/gigahorse/issues/1, Some(https://github.com/eed3si9n/gigahorse/pull/1), Some(1), Some(closed), Some(sbt-datatype 0.2.3),....
```

### search users

> Find users via various criteria. (This method returns up to 100 results per page.)

```scala
scala> val users = http.run(client(Github.search.users("eed3si9n")), Github.asUsersSearch)
users: scala.concurrent.Future[gigahorse.github.response.Paged[gigahorse.github.response.User]] = List()

scala> Await.result(users, 2.minutes)
res35: gigahorse.github.response.Paged[gigahorse.github.response.User] = Paged(Vector(User(https://api.github.com/users/eed3si9n, eed3si9n, 184683, Some(https://github.com/eed3si9n)...

scala> http.close // make sure you call this
```
