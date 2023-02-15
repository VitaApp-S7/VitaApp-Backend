# GAC Vita App

## Repo Structure

![Repository file structure](https://i.imgur.com/BmqxZAp.jpeg)

## Endpoints

### Gateway

| URL            | redirect to       | Info                                        |
| -------------- | ----------------- | ------------------------------------------- |
| /moodbooster/* | Activity service  | Prefix for all moodbooster-service requests |
| /user/*        | User service      | Prefix for all user-service requests        |
| /badge/*       | Badge service     | Prefix for all badge-service requests       |
| /challenge/*   | Challenge service | Prefix for all challenge-service requests   |
| /feed/*        | Feed service      | Prefix for all challenge-service requests   |
| /event/*       | Event service     | Prefix for all event-service requests       |

### moodbooster-service

#### moodbooster controller

| URL                                         | Funtion                        |  Type  |
| ------------------------------------------- | ------------------------------ | :----: |
| /                                           | add an moodbooster             |  POST  |
| /update                                     | update an moodbooster          |  PUT   |
| /all                                        | Get all moodboosters           |  GET   |
| /completed                                  | Get all completed moodboosters |  GET   |
| /active                                     | Get all active moodboosters    |  GET   |
| /accepted                                   | Get all accepted moodboosters  |  GET   |
| /{moodboosterId}                            | Get moodbooster by ID          |  GET   |
| /{moodboosterId}                            | Accept Moodbooster             |  PUT   |
| /cancel/{userMoodboosterId}                 | Cancel Moodbooster             |  PUT   |
| /complete/{userMoodboosterId}               | Mark Moodbooster as complete   |  PUT   |
| /{moodboosterId}                            | Delete moodbooster by ID       | DELETE |
| /invites                                    | Get all moodbooster invites    |  GET   |
| /invite/{userMoodboosterId}/{invitedUserId} | Invite user for moodbooster    |  POST  |
| /invite/accept/{inviteId}                   | accept moodbooster invite      |  POST  |
| /invite/decline/{inviteId}                  | decline moodbooster invite     | DELETE |

#### Category controller

| URL            | Function           |  Type  |
| -------------- | ------------------ | :----: |
| /category      | Add category       |  POST  |
| /category/all  | Get all categories |  GET   |
| /category/{id} | Get category       |  GET   |
| /category      | Update category    |  PUT   |
| /category/{id} | Delete category    | DELETE |

#### stats controller

| URL                    | Function                      | Type  |
| ---------------------- | ----------------------------- | :---: |
| stats/moodboosterusage | Get the usage of moodboosters |  GET  |

### Badge service

#### Badge controller

| URL               | Function                     |  Type  | Request Information                                 |
| ----------------- | ---------------------------- | :----: | --------------------------------------------------- |
| /                 | Create badge                 |  POST  | Post 2 files: Image and .json file with information |
| /all              | Get all Badges               |  GET   |                                                     |
| /{id}             | Get badge by ID              |  GET   |                                                     |
| /{id}/user        | Get userBadge by id          |  GET   |                                                     |
| /                 | Update Badge                 |  PUT   | PUT 2 files: Image and .json file with information  |
| /{id}             | Delete badge                 | DELETE |                                                     |
| /completeactivity | Test endpoint for subscriber |  POST  |                                                     |
| /newuser          | Test endpoint for subscriber |  POST  |                                                     |

### Challenge service

#### Challenge controller

| URL                                | Function                      |  Type  |
| ---------------------------------- | ----------------------------- | :----: |
| /all/pages                         | Get all challenges in pages   |  GET   |
| /all                               | Get all challenges            |  GET   |
| /available                         | Get all available challenges  |  GET   |
| /{id}                              | Get challenge by id           |  GET   |
| /                                  | Add a challenge               |  POST  |
| /                                  | Update a challenge            |  PUT   |
| /{id}                              | Delete a challenge            | DELETE |
| /accept/{userChallengeId}          | Accept a challenge            |  PUT   |
| /{userChallengeId}/invite/{userId} | Invite a user to a challenge  |  PUT   |
| /open/{challengeId}                | Open a challenge              |  POST  |
| /start/{userChallengeId}           | Start a challenge             |  PUT   |
| /cancel/{id}                       | Cancel a challenge            |  PUT   |
| /active                            | Get active challenges by user |  GET   |

### User service

#### User controller

| URL                            | Function                                                                                                                             | Type  |
| ------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------ | :---: |
| /all                           | Get all users as an admin                                                                                                            |  GET  |
| /{userId}                      | Get user by ID                                                                                                                       |  GET  |
| /public/page/{pageNumber}      | Get public users per page                                                                                                            |  GET  |
| /public/all					 | Get All Public Users  																												|  GET  |
| /setmood/{amount}              | Set user mood                                                                                                                        | POST  |
| /decreasemood/{decreaseAmount} | Decrease user mood                                                                                                                   | POST  |
| /login/check                   | On login, checks if this is the user's first time. If it is, the user data is parsed from the token claims and saved to the database |  GET  |
| /me                            | Returns user info of the authenticated user                                                                                          |  GET  |
| /setexpo/{token}               | Set user ExpoToken                                                                                                                   | POST  |
| /expo                          | Get User ExpoToken                                                                                                                   |  GET  |
| /date/{date}                   | Set user date                                                                                                                        | POST  |
| /date                          | Get User date                                                                                                                        |  GET  |
| /modalvisible/{bool}           | Set User model visible to true or false                                                                                              | POST  |
| /modalvisible                  | Get User model visible                                                                                                               |  GET  |

#### Friend controller

| URL                                        | Function             |  Type  |
| ------------------------------------------ | -------------------- | :----: |
| /friends/add/{friendId}                    | Send friend Request  |  POST  |
| /friends/requests                          | Get friendrequests   |  GET   |
| /sendedrequests                            | Get Sended requests  |  GET   |
| /friends/requests/accept/{friendRequestId} | Accept friendrequest |  POST  |
| /friends/requests/cancel/{friendRequestId} | Cancel friendrequest | DELETE |
| /friends                                   | Get all friends      |  GET   |
| /friends/remove/{friendObjectId}           | remove friend        | DELETE |

#### Notification controller

| URL               | Function                               | Type  |
| ----------------- | -------------------------------------- | :---: |
| /testnotification | Send test notification to current user | POST  |

### Feed service

#### Feed controller

| URL            | Function          |  Type  |
| -------------- | ----------------- | :----: |
| /feed/         | Add a newsItem    |  POST  |
| /feed/{newsId} | Get a newsItem    |  GET   |
| /feed/{newsId} | Delete a newsItem | DELETE |
| /feed/all      | Get all newsItems |  GET   |

### Event service

#### Event controller

| URL                   | Function                                                                                                   |  Type  |
| --------------------- | ---------------------------------------------------------------------------------------------------------- | :----: |
| /all                  | Get all Events                                                                                             |  GET   |
| /add                  | Adds a Event                                                                                               |  POST  |
| /{eventId}            | Deletes a Event                                                                                            | DELETE |
| /check-user/{eventId} | Checks if user is in a event. If not, add user. If the user is in a event then remove user from that event |  PUT   |
| /join/{eventId}       | Adds logged in user to event                                                                               |  PUT   |
| /leave/{eventId}      | Removes logged in user from event                                                                          |  PUT   |
