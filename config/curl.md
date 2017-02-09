### couple curl commands to test REST API

#### get all robots
`curl "http://robotsworld.herokuapp.com/rest/robots/all"`

#### send personal task
`curl -X PUT "http://robotsworld.herokuapp.com/rest/robots/ProgRobot-0/tasks/1"`

#### send broadcast task
`curl -X PUT "http://robotsworld.herokuapp.com/rest/robots/all/tasks/2"`

#### add new robot
`curl -X POST "http://robotsworld.herokuapp.com/rest/robots" --data "id=3"`

#### kill robot
`curl -X DELETE "http://robotsworld.herokuapp.com/rest/robots/ProgRobot-0"`

#### get last activiry (25 - number of lines, en - language (ru, en))
`curl "http://robotsworld.herokuapp.com/rest/logs/25/language/en"`

#### ids for tasks 
    id = 0 : for Programmer,
    id = 1 : for Mathematician,
    id = 2 : for Scientist,
    id = 3 : for Builder,
    id = 4 : for Singer,
    id = 5 : Suicide:
