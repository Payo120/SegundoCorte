# Matriz de Pruebas y Evidencias – Papelería Campus (JavaFX + PostgreSQL)

Documentación oficial del conjunto de pruebas funcionales, validaciones de entrada, manejo de excepciones y operaciones del ciclo CRUD implementadas en el sistema de gestión de la papelería Campus.

---

## 1. Listado Inicial y Conexión con PostgreSQL
- **Descripción:** Verificación de la conectividad JDBC y la carga inicial de registros desde la base de datos relacional.
- **Resultado Esperado:** La aplicación establece conexión exitosa con el servidor PostgreSQL y carga de forma automática los registros iniciales en el `TableView`, mostrando las 6 columnas correspondientes (`ID`, `Código`, `Nombre`, `Categoría`, `Precio`, `Stock`) y el contador global de registros.
- **Estado:** Exitoso.

---

## 2. Búsquedas y Filtros Dinámicos (Casos B1 - B7)
- **B1 (Búsqueda por texto - "CUADERNO"):** Filtra de manera precisa y muestra únicamente los productos coincidentes (P001 y P002). Contador actualizado a 2 filas visibles. *(Exitoso)*
- **B2 (Búsqueda parcial - "T00"):** Filtra por coincidencia parcial de prefijo en el código, mostrando los productos T001 a T004. Contador actualizado a 4. *(Exitoso)*
- **B3 (Filtro por Categoría - "Papelería"):** Aisla y muestra exclusivamente los 4 registros pertenecientes a la categoría seleccionada en el ComboBox. *(Exitoso)*
- **B4 (Filtro de Existencias - "Solo con existencias"):** Oculta de forma automática los productos cuyo stock se encuentra en 0 (P004, T004, A003), mostrando únicamente los 9 productos con disponibilidad activa. *(Exitoso)*
- **B5 (Filtro Combinado - Texto + Categoría + Existencias):** Aplica múltiples criterios de manera simultánea (ej. texto parcial, categoría tecnología y stock activo), devolviendo los registros exactos esperados. *(Exitoso)*
- **B6 (Búsqueda sin coincidencias - "ZZZ999"):** Oculta todas las filas del catálogo, reduce el contador a 0 y despliega el aviso visual correspondiente de "Sin coincidencias". *(Exitoso)*
- **B7 (Limpieza de Filtros):** Restablece todos los campos de búsqueda, categorías y casillas de verificación a su estado por defecto, restaurando la visualización completa del catálogo original. *(Exitoso)*

---

## 3. Operaciones CRUD y Sincronización (Caso C5)
- **Create (Crear):** Inserción exitosa de un nuevo producto mediante el formulario (ej. Código `X001`, Nombre "Producto de prueba", Categoría "Accesorios", Precio `10.50`, Stock `10`). El sistema actualiza el `TableView` de inmediato y persiste el registro en PostgreSQL. *(Exitoso)*
- **Update (Actualizar):** Selección de un registro existente, modificación de sus atributos (precio y stock) y persistencia mediante sentencia parametrizada. Los cambios se reflejan al instante en la interfaz gráfica y en la base de datos. *(Exitoso)*
- **Delete (Eliminar):** Solicitud de borrado sobre un producto seleccionado. El sistema despliega un diálogo de confirmación obligatorio; al cancelar, se preserva el registro, y al aceptar, se elimina de manera permanente tanto de la interfaz como del servidor PostgreSQL. *(Exitoso)*

---

## 4. Validaciones de Entrada y Manejo de Excepciones (V1 - V10 y E1 - E4)
- **V1 - V8 (Validaciones de formulario):** Rechazo preventivo y control mediante alertas emergentes ante códigos vacíos o mayores a 10 caracteres, nombres fuera del rango permitido (3 a 80), categorías no seleccionadas, precios negativos, uso de comas decimales o stocks inválidos. *(Exitoso)*
- **V9 (Códigos duplicados):** Validación estricta que bloquea el registro e informa al usuario si se intenta ingresar un código de producto que ya existe en la base de datos (normalizado a mayúsculas). *(Exitoso)*
- **V10 (Selección obligatoria):** Restricción lógica que deshabilita o impide la ejecución de acciones de actualización o eliminación si el usuario no ha seleccionado previamente una fila en el `TableView`. *(Exitoso)*
- **E1 - E4 (Gestión de excepciones SQL):** Captura centralizada de errores a nivel de base de datos (violaciones de restricciones de integridad `CHECK` o `UNIQUE`, fallos de conectividad JDBC) imprimiendo los códigos `SQLState` correspondientes en la consola del sistema y notificando al usuario de forma controlada. *(Exitoso)*
