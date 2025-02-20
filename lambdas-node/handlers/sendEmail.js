const { SNSClient, PublishCommand } = require("@aws-sdk/client-sns");
const { SQSClient, DeleteMessageCommand } = require("@aws-sdk/client-sqs");

const snsClient = new SNSClient({});
const sqsClient = new SQSClient({});

exports.sendEmail = async (event) => {
  const messages = event.Records;

  if (messages && messages.length > 0) {
    for (const message of messages) {
      const body = JSON.parse(message.body);

      try {
        await sendMessageToSNS(body);
        await deleteMessageFromSQS(message);
      } catch (error) {
        console.error("Error procesando el mensaje:", error);
      }
    }
  }
};

async function sendMessageToSNS(body) {
  await snsClient.send(
    new PublishCommand({
      Message: JSON.stringify(body),
      TopicArn: process.env.TOPIC_ARN,
    })
  );
}

async function deleteMessageFromSQS(message) {
  const queueUrl = process.env.SQS_QUEUE_URL;

  const params = {
    QueueUrl: queueUrl,
    ReceiptHandle: message.receiptHandle,
  };

  await sqsClient.send(new DeleteMessageCommand(params));
}
