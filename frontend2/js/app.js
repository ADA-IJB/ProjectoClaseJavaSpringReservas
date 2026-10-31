/* ============================================================
   Configuración de cada entidad.
   Con estos datos se pintan las tablas y los formularios,
   así no hay que repetir el mismo código para cada una.
   ============================================================ */
const hora = (h) => (h ? h.slice(0, 5) : ""); // "08:00:00" -> "08:00"
const nombreProfesor = (p) => `${p.nombre} ${p.apellidos}`;
const textoFranja = (f) => `${hora(f.horaInicio)} - ${hora(f.horaFin)}`;

const ENTIDADES = {
  aulas: {
    ruta: "/aulas",
    nuevo: "Nueva aula",
    editar: "Editar aula",
    campos: [
      { name: "numeroClase", label: "Numero Clase", type: "number", step: "1", min: "0" },
      { name: "capacidad", label: "Capacidad", type: "number", step: "1", min: "0" },
      { name: "tieneOrdenadores", label: "¿Tiene Ordenadores?", type: "checkbox" },
    ],
    columnas: [
      { titulo: "Id", valor: (a) => a.id },
      { titulo: "Numero Clase", valor: (a) => a.numeroClase },
      { titulo: "Capacidad", valor: (a) => a.capacidad },
      { titulo: "Ordenadores", valor: (a) => (a.tieneOrdenadores ? "Sí" : "No") },
    ],
  },
  profesores: {
    ruta: "/profesores",
    nuevo: "Nuevo profesor",
    editar: "Editar profesor",
    campos: [
      { name: "nombre", label: "Nombre" },
      { name: "apellidos", label: "Apellidos" },
      { name: "dni", label: "DNI", maxlength: 10 },
      { name: "sueldo", label: "Sueldo", type: "number", step: "0.01", min: "0" },
      { name: "fechaNacimiento", label: "Fecha Nacimiento", type: "date" },
      { name: "fechaIncorporacion", label: "Fecha Incorporacion", type: "date" },
    ],
    columnas: [
      { titulo: "Id", valor: (p) => p.id },
      { titulo: "Nombre", valor: (p) => p.nombre },
      { titulo: "Apellidos", valor: (p) => p.apellidos },
      { titulo: "Dni", valor: (p) => p.dni },
      { titulo: "Sueldo", valor: (p) => p.dni },
      { titulo: "Fecha Nacimiento", valor: (p) => p.fechaNacimiento },
      { titulo: "Fecha Incorporacion", valor: (p) => p.fechaIncorporacion },
    ],
  },
  franjas: {
    ruta: "/franjasHorarias",
    nuevo: "Nueva franja horaria",
    editar: "Editar franja horaria",
    campos: [
      { name: "horaInicio", label: "Hora de inicio", type: "time", required: true },
      { name: "horaFin", label: "Hora de fin", type: "time", required: true },
    ],
    columnas: [
      { titulo: "Id", valor: (f) => f.id },
      { titulo: "Inicio", valor: (f) => hora(f.horaInicio) },
      { titulo: "Fin", valor: (f) => hora(f.horaFin) },
    ],
  },
  reservas: {
    ruta: "/reservas",
    // Las reservas se crean con el formulario de su pestaña y no se editan desde aquí
    columnas: [
      { titulo: "Id", valor: (r) => r.id },
      { titulo: "Fecha", valor: (r) => r.fecha },
      { titulo: "Aula", valor: (r) => r.aula.numeroClase },
      { titulo: "Profesor", valor: (r) => nombreProfesor(r.profesor) },
      { titulo: "Franja", valor: (r) => textoFranja(r.franjaHoraria) },
      { titulo: "Realizada el", valor: (r) => new Date(r.fechaInsercion).toLocaleString("es-ES") },
    ],
  },
};

// Aquí se guardan los datos que nos devuelve el backend
const datos = { aulas: [], profesores: [], franjas: [], reservas: [] };

/* ============================================================
   Utilidades
   ============================================================ */
const $ = (id) => document.getElementById(id);

// Crea un elemento HTML: el("td", { textContent: "hola" })
function el(etiqueta, props = {}, ...hijos) {
  const e = Object.assign(document.createElement(etiqueta), props);
  e.append(...hijos);
  return e;
}

function mostrarMensaje(texto, error = false) {
  const m = $("mensaje");
  m.textContent = texto;
  m.className = "mensaje" + (error ? " error" : "");
  clearTimeout(mostrarMensaje.temporizador);
  mostrarMensaje.temporizador = setTimeout(() => m.classList.add("oculto"), 5000);
}

// Ejecuta una acción y muestra el resultado. Si el backend devuelve un error, se muestra su mensaje.
async function ejecutar(accion, textoOk) {
  try {
    await accion();
    if (textoOk) mostrarMensaje(textoOk);
  } catch (e) {
    mostrarMensaje(e.message, true);
  }
}

const hoy = () => new Date().toLocaleDateString("sv-SE"); // fecha local en formato AAAA-MM-DD

/* ============================================================
   Carga y pintado de tablas
   ============================================================ */
async function cargar(clave) {
  datos[clave] = await api.listar(ENTIDADES[clave].ruta);
  pintarTabla(clave);
  if (clave !== "reservas") rellenarSelectsReserva();
}

function pintarTabla(clave) {
  const entidad = ENTIDADES[clave];
  const contenedor = $("tabla-" + clave);

  if (datos[clave].length === 0) {
    contenedor.replaceChildren(el("p", { className: "vacio", textContent: "No hay registros todavía." }));
    return;
  }

  const cabecera = el("tr", {}, ...entidad.columnas.map((c) => el("th", { textContent: c.titulo })), el("th"));
  const filas = datos[clave].map((item) => {
    const acciones = el("td", { className: "acciones-fila" });
    if (entidad.campos) {
      acciones.append(el("button", { textContent: "Editar", onclick: () => editar(clave, item) }));
    }
    acciones.append(el("button", { className: "peligro", textContent: "Borrar", onclick: () => borrar(clave, item) }));
    return el("tr", {}, ...entidad.columnas.map((c) => el("td", { textContent: c.valor(item) })), acciones);
  });

  contenedor.replaceChildren(el("table", {}, el("thead", {}, cabecera), el("tbody", {}, ...filas)));
}

/* ============================================================
   Crear, editar y borrar (aulas, profesores y franjas)
   ============================================================ */
function abrirFormulario(titulo, campos, valores, alGuardar) {
  $("dialogo-titulo").textContent = titulo;
  const contenedor = $("dialogo-campos");
  contenedor.replaceChildren();

  for (const c of campos) {
    const { label, ...atributos } = c;
    const control = el("input", atributos);
    if (c.type === "checkbox") control.checked = !!valores[c.name];
    else control.value = valores[c.name] ?? "";
    contenedor.append(el("label", { className: c.type === "checkbox" ? "check" : "" }, label, control));
  }

  $("form-dialogo").onsubmit = (ev) => {
    ev.preventDefault();
    // Convertimos lo escrito en el formulario en un objeto con el tipo adecuado
    const objeto = {};
    for (const c of campos) {
      const control = ev.target.elements[c.name];
      if (c.type === "checkbox") objeto[c.name] = control.checked;
      else if (c.type === "number") objeto[c.name] = control.value === "" ? null : Number(control.value);
      else objeto[c.name] = control.value === "" ? null : control.value;
    }
    $("dialogo").close();
    alGuardar(objeto);
  };
  $("dialogo").showModal();
}

$("dialogo-cancelar").onclick = () => $("dialogo").close();

function nuevo(clave) {
  const entidad = ENTIDADES[clave];
  abrirFormulario(entidad.nuevo, entidad.campos, {}, (objeto) =>
    ejecutar(async () => {
      await api.crear(entidad.ruta, objeto);
      await cargar(clave);
    }, "Registro creado")
  );
}

function editar(clave, item) {
  const entidad = ENTIDADES[clave];
  abrirFormulario(entidad.editar, entidad.campos, item, (objeto) =>
    ejecutar(async () => {
      await api.actualizar(entidad.ruta, item.id, objeto);
      await cargar(clave);
    }, "Registro actualizado")
  );
}

function borrar(clave, item) {
  const aviso = clave === "reservas"
    ? "¿Borrar esta reserva?"
    : "¿Borrar este registro? También se borrarán sus reservas.";
  if (!confirm(aviso)) return;

  ejecutar(async () => {
    await api.borrar(ENTIDADES[clave].ruta, item.id);
    await cargar(clave);
    if (clave !== "reservas") await cargar("reservas"); // el borrado en cascada elimina reservas
  }, "Registro borrado");
}

/* ============================================================
   Reservas
   ============================================================ */
function rellenarSelect(select, lista, texto) {
  const seleccionado = select.value;
  select.replaceChildren(
    el("option", { value: "", textContent: "-- Selecciona --" }),
    ...lista.map((x) => el("option", { value: x.id, textContent: texto(x) }))
  );
  select.value = seleccionado;
}

function rellenarSelectsReserva() {
  const f = $("form-reserva").elements;
  rellenarSelect(f.aula, datos.aulas, (a) => `${a.numeroClase} (${a.capacidad} alumnos)`);
  rellenarSelect(f.profesor, datos.profesores, nombreProfesor);
  rellenarSelect(f.franja, datos.franjas, textoFranja);
}

$("form-reserva").onsubmit = (ev) => {
  ev.preventDefault();
  const f = ev.target.elements;


  // El backend espera solo los ids de aula, profesor y franja
  const reserva = {
    fecha: f.fechaReserva.value,
    aula: { id: Number(f.aula.value) },
    profesor: { id: Number(f.profesor.value) },
    franjaHoraria: { id: Number(f.franja.value) },
  };

  ejecutar(async () => {
    await api.crear("/reservas", reserva);
    await cargar("reservas");
  }, "Reserva creada");
};

/* ============================================================
   Consulta de un aula con filtros: /aulas/{id}?ordenadores=true  o  /aulas/{id}?capacidad=20
   ============================================================ */
const formConsulta = $("form-consulta");

formConsulta.elements.filtro.onchange = (ev) => {
  $("etiqueta-capacidad").classList.toggle("oculto", ev.target.value !== "capacidad");
  formConsulta.elements.capacidad.required = ev.target.value === "capacidad";
};

formConsulta.onsubmit = async (ev) => {
  ev.preventDefault();
  const f = formConsulta.elements;

  let ruta = `/aulas`;
  if (f.filtro.value === "ordenadores-si") ruta += "?tieneOrdenadores=true";
  if (f.filtro.value === "ordenadores-no") ruta += "?tieneOrdenadores=false";
  if (f.filtro.value === "capacidad") ruta += `?capacidad=${f.capacidad.value}`;

  const resultado = $("resultado-consulta");
  resultado.className = "resultado";
  try {
    const aula = await api.listar(ruta);
    resultado.textContent = `GET ${ruta} → 200: \n`;
    for (let i in aula) {
      resultado.textContent += JSON.stringify(aula[i]);
      resultado.textContent += "\n";
    }
  } catch (e) {
    resultado.classList.add("error");
    resultado.textContent = e.status === 404
      ? `GET ${ruta} → 404: el aula no existe o no cumple el filtro`
      : `GET ${ruta} → ${e.status ?? ""} ${e.message}`;
  }
};

/* ============================================================
   Pestañas y arranque
   ============================================================ */
document.querySelectorAll(".tab").forEach((boton) => {
  boton.onclick = () => {
    document.querySelectorAll(".tab").forEach((b) => b.classList.toggle("active", b === boton));
    document.querySelectorAll(".panel").forEach((p) => p.classList.toggle("oculto", p.id !== boton.dataset.tab));
  };
});

for (const clave of ["aulas", "profesores", "franjas"]) {
  $("nuevo-" + clave).onclick = () => nuevo(clave);
}

$("form-reserva").elements.fechaReserva.value = hoy();

ejecutar(async () => {
  // Primero lo que necesitan los selects y después las reservas
  await Promise.all([cargar("aulas"), cargar("profesores"), cargar("franjas")]);
  await cargar("reservas");
});
