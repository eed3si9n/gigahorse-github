package repatch.github

package object request {
  def repo(owner: String, name: String): Repos = Repos(owner, name)
  def issues: Issues = Issues(Map())
  def user: User = User()
  def user(name: String): Users = Users(name)
  def url(u: String): UrlMethod = UrlMethod(u)
}
