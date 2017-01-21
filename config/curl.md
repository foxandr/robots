### couple curl commands to test REST API

#### get all robots
`curl "http://localhost:8080/rest/get"`

#### send personal task
`curl -X POST "http://localhost:8080/rest/personal" --data "name=ProgRobot-0&id=1"`

#### send broadcast task
`curl -X POST "http://localhost:8080/rest/broadcast" --data "id=2"`

#### add new robot
`curl -X POST "http://localhost:8080/rest/add" --data "id=3"`

#### kill robot
`curl -X POST "http://localhost:8080/rest/kill" --data "suicide=ProgRobot-0"`

#### get last activiry (num - number of lines, lang - language (ru, en))
`curl "http://localhost:8080/rest/logs?num=25&lang=en"`

#### ids for tasks 
    id = 0 : for Programmer,
    id = 1 : for Mathematician,
    id = 2 : for Scientist,
    id = 3 : for Builder,
    id = 4 : for Singer,
    id = 5 : Suicide:
