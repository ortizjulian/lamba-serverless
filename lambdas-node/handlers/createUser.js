exports.createUsers = async (event) => {
  const users = [
    { id: 1, name: "Julian", email: "julian.ortixs@gmail.com" },
    { id: 2, name: "Julio", email: "julian.ortixs@gmail.com" },
    { id: 3, name: "Juli", email: "julian.ortixs@gmail.com" },
  ];

  if (!event.body) {
    return {
      statusCode: 400,
      body: JSON.stringify({
        message: "El cuerpo de la solicitud está vacío.",
      }),
    };
  }

  const newUser = JSON.parse(event.body);

  if (!newUser.id || !newUser.name || !newUser.email) {
    return {
      statusCode: 400,
      body: JSON.stringify({
        message: "El usuario debe tener 'id', 'name' y 'email'.",
      }),
    };
  }

  users.push(newUser);

  return {
    statusCode: 200,
    body: JSON.stringify({
      message: "Usuario agregado con éxito",
      users: users,
    }),
  };
};
