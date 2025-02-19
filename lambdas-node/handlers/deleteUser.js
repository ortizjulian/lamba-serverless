const { DynamoDBClient } = require("@aws-sdk/client-dynamodb");
const {
  DynamoDBDocumentClient,
  GetCommand,
  DeleteCommand,
} = require("@aws-sdk/lib-dynamodb");
const client = new DynamoDBClient({});

const dynamo = DynamoDBDocumentClient.from(client);

const tableName = process.env.DYNAMODB_TABLE_NAME;

exports.deleteUser = async (event) => {
  let body;
  body = await dynamo.send(
    new GetCommand({
      TableName: tableName,
      Key: {
        id: event.pathParameters.id,
      },
    })
  );

  if (!body.Item) {
    return {
      statusCode: 404,
      body: JSON.stringify({
        message: "User not found",
      }),
    };
  }

  await dynamo.send(
    new DeleteCommand({
      TableName: tableName,
      Key: {
        id: event.pathParameters.id,
      },
    })
  );

  return {
    statusCode: 200,
    body: JSON.stringify({
      message: "User deleted successfully",
    }),
  };
};
