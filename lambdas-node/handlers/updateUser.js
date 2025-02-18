const { DynamoDBClient } = require("@aws-sdk/client-dynamodb");
const {
  DynamoDBDocumentClient,
  GetCommand,
  UpdateCommand,
} = require("@aws-sdk/lib-dynamodb");

const client = new DynamoDBClient({});

const dynamo = DynamoDBDocumentClient.from(client);

const tableName = "user";

exports.updateUser = async (event) => {
  const user = event.body ? JSON.parse(event.body) : null;

  if (!user) {
    return {
      statusCode: 400,
      body: JSON.stringify({
        message: "El cuerpo de la solicitud no puede estar vacío.",
      }),
    };
  }

  if (!user.id || !user.name || !user.email) {
    return {
      statusCode: 400,
      body: JSON.stringify({
        message: "El usuario debe tener 'id', 'name' y 'email'.",
      }),
    };
  }
  const userId = user.id;
  let body;
  body = await dynamo.send(
    new GetCommand({
      TableName: tableName,
      Key: {
        id: userId,
      },
    })
  );

  if (!body.Item) {
    return {
      statusCode: 404,
      body: JSON.stringify({
        message: "Usuario no encontrado",
      }),
    };
  }

  const updateParams = {
    TableName: "user",
    Key: { id: userId },
    UpdateExpression: "set #name = :name, #email = :email",
    ExpressionAttributeNames: {
      "#name": "name",
      "#email": "email",
    },
    ExpressionAttributeValues: {
      ":name": user.name,
      ":email": user.email,
    },
    ReturnValues: "ALL_NEW",
  };

  const updatedUser = await dynamo.send(new UpdateCommand(updateParams));

  return {
    statusCode: 200,
    body: JSON.stringify({
      message: "Usuario actualizado",
      user: updatedUser.Attributes,
    }),
  };
};
