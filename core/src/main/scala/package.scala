package repatch.github

package object request {
  def repo(owner: String, name: String): Repos = Repos(owner, name)
  def issues: Issues = Issues(Map())
  def user: Users = Users(None)
  def user(name: String): Users = Users(Some(name))
  def url(u: String): UrlMethod = UrlMethod(u)
  def search: Search = Search()
}
