exports.updateUsers = async (event) => {
  const users = [
    { id: 1, name: "Julian", email: "julian.ortixs@gmail.com" },
    { id: 2, name: "Julio", email: "julian.ortixs@gmail.com" },
    { id: 3, name: "Juli", email: "julian.ortixs@gmail.com" },
  ];

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

  const index = users.findIndex((u) => u.id === user.id);

  if (index === -1) {
    return {
      statusCode: 404,
      body: JSON.stringify({
        message: "Usuario no encontrado",
      }),
    };
  }

  users[index] = { ...users[index], ...user };

  return {
    statusCode: 200,
    body: JSON.stringify({
      message: "Usuario actualizado",
      user: users[index],
    }),
  };
};
