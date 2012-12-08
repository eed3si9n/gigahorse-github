
## setting OAuth token to git config

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
