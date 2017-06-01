# TwilioPlayService

Currently the following REST calls are supported:

    POST    /auth/login (com.controlledthinking.dropwizard.resources.AuthResource)
    PUT     /customers (com.controlledthinking.dropwizard.resources.CustomerResource)
    GET     /customers/group/{groupId} (com.controlledthinking.dropwizard.resources.CustomerResource)
    GET     /customers/user (com.controlledthinking.dropwizard.resources.CustomerResource)
    DELETE  /customers/{customerId} (com.controlledthinking.dropwizard.resources.CustomerResource)
    PUT     /groups (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    GET     /groups/user/{userId} (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    DELETE  /groups/{groupId} (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    POST    /groups/{groupId}/customers (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    DELETE  /groups/{groupId}/customers/{customerId} (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    GET     /numbers (com.controlledthinking.dropwizard.resources.PhoneNumberResource)
    GET     /numbers/user (com.controlledthinking.dropwizard.resources.PhoneNumberResource)

All resources are currently protected and need to login.  POST to /auth/login to get the JWT that must be set in a "X-auth" header on subsequent requests.

Look in "core" package for model classes which should be JSON to send to PUTs and POSTs.
