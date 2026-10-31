// Función base: hace la petición y, si el servidor responde con un error (400, 404, 409...),
// lanza una excepción con el texto que haya enviado el backend.
async function peticion(ruta, opciones = {}) {
  const respuesta = await fetch(API_URL + ruta, {
    headers: { "Content-Type": "application/json" },
    ...opciones,
  });

  if (!respuesta.ok) {
    const texto = await respuesta.text(); // El backend manda un mensaje en los errores 400 y 409
    const error = new Error(texto || `Error ${respuesta.status} en ${opciones.method || "GET"} ${ruta}`);
    error.status = respuesta.status;
    throw error;
  }
  // 204 No Content (borrado) no trae cuerpo
  return respuesta.status === 204 ? null : respuesta.json();
}

const enviar = (metodo, ruta, cuerpo) =>
  peticion(ruta, { method: metodo, body: JSON.stringify(cuerpo) });

// Las mismas cinco operaciones sirven para todas las entidades: solo cambia la ruta
const api = {
  listar: (ruta) => peticion(ruta),
  crear: (ruta, objeto) => enviar("POST", ruta, objeto),
  actualizar: (ruta, id, objeto) => enviar("PUT", `${ruta}/${id}`, objeto),
  borrar: (ruta, id) => peticion(`${ruta}/${id}`, { method: "DELETE" }),
};
