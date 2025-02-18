exports.getUser = async (event) => {
  const users = [
    { id: 1, name: "Julian", email: "julian.ortixs@gmail.com" },
    { id: 2, name: "Julio", email: "julian.ortixs@gmail.com" },
    { id: 3, name: "Juli", email: "julian.ortixs@gmail.com" },
  ];

  const user = users.find((u) => u.id === parseInt(event.pathParameters.id));

  if (!user) {
    return {
      statusCode: 404,
      body: JSON.stringify({ message: "Usuario no encontrado" }),
    };
  }

  return {
    statusCode: 200,
    body: JSON.stringify(user),
  };
};
