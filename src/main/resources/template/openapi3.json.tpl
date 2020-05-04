{
    "openapi": "3.0.0",
    "info": {
        "title": "tutorial-api",
        "version": "1.0.0"
    },
    "paths": {
        "/session": {
            "get": {
                "summary": "Get all session messages",
                "description": "Return all session messages",
                "operationId": "API_SESSION_GET",
                "responses": {
                    "200": {
                        "description": "Return all messages",
                        "content": {
                            "application/json": {
                                "schema": {
                                    "type": "array",
                                    "items": {
                                        "$ref": "#/components/schemas/message"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    },
    "components": {
        "schemas": {
            "message": {
                "title": "Root Type for message",
                "description": "The root of the message type's schema.",
                "type": "object",
                "properties": {
                    "message": {
                        "type": "string"
                    },
                    "time": {
                        "type": "string"
                    }
                }
            }
        }
    }
}