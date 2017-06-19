# TwilioPlayService

Currently the following REST calls are supported:

    POST    /auth/login (com.controlledthinking.dropwizard.resources.AuthResource)
    POST    /groups/{groupId}/customers (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    PUT     /customers (com.controlledthinking.dropwizard.resources.CustomerResource)
    PUT     /groups (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    PUT     /message/customer/{customerId} (com.controlledthinking.dropwizard.resources.ImmediateMessageResource)
    PUT     /message/group/{groupId} (com.controlledthinking.dropwizard.resources.ImmediateMessageResource)
    GET     /customers/group/{groupId} (com.controlledthinking.dropwizard.resources.CustomerResource)
    GET     /customers/user (com.controlledthinking.dropwizard.resources.CustomerResource)
    GET     /groups/user/{userId} (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    GET     /numbers (com.controlledthinking.dropwizard.resources.PhoneNumberResource)
    GET     /numbers/user (com.controlledthinking.dropwizard.resources.PhoneNumberResource)
    DELETE  /groups/{groupId} (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    DELETE  /groups/{groupId}/customers/{customerId} (com.controlledthinking.dropwizard.resources.MessageGroupResource)
    DELETE  /customers/{customerId} (com.controlledthinking.dropwizard.resources.CustomerResource)


All resources are currently protected and need to login.  POST to /auth/login to get the JWT that must be set in a "X-auth" header on subsequent requests.

Look in "core" package for model classes which should be JSON to send to PUTs and POSTs.
