package repatch.github

package object request {
  def repo(owner: String, name: String): Repos = Repos(owner, name)
  def issues: Issues = Issues(Map())
}
