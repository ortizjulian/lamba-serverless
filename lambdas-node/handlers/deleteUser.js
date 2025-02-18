exports.deleteUser = async (event) => {
  const users = [
    { id: 1, name: "Julian", email: "julian.ortixs@gmail.com" },
    { id: 2, name: "Julio", email: "julian.ortixs@gmail.com" },
    { id: 3, name: "Juli", email: "julian.ortixs@gmail.com" },
  ];

  const userId = parseInt(event.pathParameters.id);

  const userIndex = users.findIndex((u) => u.id === userId);

  if (userIndex === -1) {
    return {
      statusCode: 404,
      body: JSON.stringify({ message: "Usuario no encontrado" }),
    };
  }

  users.splice(userIndex, 1);

  return {
    statusCode: 200,
    body: JSON.stringify({ message: "Usuario eliminado con éxito" }),
  };
};
