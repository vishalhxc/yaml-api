# gokapio
## JVM library to send http requests from yaml files

### Sample request format
```yaml
name: name-to-identify-request
environment: dev
method: get
url: {base-url}/path/{path-parameter}?query={query-parameter}
headers:
  - authorization: Bearer token
  - content-type: application/json
  - accept: application/json
body: >-
  {
    "field1": "{field-one}",
    "field2": "{field-two}",
    "field3": {field-three}
  }
variables:
  - path-parameter: abc123
  - query-parameter: true
  - field-one: foo
  - field-two: bar
  - field-three: 33
  environment:
    dev: 
      - base-url: http://dev.example.com
    prod: 
      - base-url: http://www.example.com
      - user-id: somerealuser99
```