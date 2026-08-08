
const API_URL = "http://localhost:8080/orders";

const createOrderForm = document.getElementById("createOrderForm");

const orderIdInput = document.getElementById("orderId");

const getOrderButton = document.getElementById("getOrderButton");
const confirmOrderButton = document.getElementById("confirmOrderButton");
const cancelOrderButton = document.getElementById("cancelOrderButton");

const alertContainer = document.getElementById("alertContainer");

const orderResult = document.getElementById("orderResult");
const emptyResult = document.getElementById("emptyResult");

const resultId = document.getElementById("resultId");
const resultCustomer = document.getElementById("resultCustomer");
const resultTotal = document.getElementById("resultTotal");
const resultStatus = document.getElementById("resultStatus");


/**
 * Muestra una alerta Bootstrap.
 */
function showAlert(message, type = "success") {

    alertContainer.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button
                type="button"
                class="btn-close"
                data-bs-dismiss="alert">
            </button>
        </div>
    `;
}


/**
 * Ejecuta una petición HTTP y maneja errores.
 */
async function apiRequest(url, options = {}) {

    const response = await fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    });

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {

        const message =
            data.error ||
            "Ocurrió un error al procesar la solicitud.";

        throw new Error(message);
    }

    return data;
}


/**
 * Muestra la información de un pedido.
 */
function displayOrder(order) {

    resultId.textContent = order.id;
    resultCustomer.textContent = order.customerId;

    resultTotal.textContent =
        Number(order.total).toLocaleString("es-CO", {
            style: "currency",
            currency: "COP"
        });

    resultStatus.textContent = order.status;

    resultStatus.className = "badge";

    switch (order.status) {

        case "PENDING":
            resultStatus.classList.add("bg-warning", "text-dark");
            break;

        case "CONFIRMED":
            resultStatus.classList.add("bg-success");
            break;

        case "CANCELLED":
            resultStatus.classList.add("bg-danger");
            break;

        default:
            resultStatus.classList.add("bg-secondary");
    }

    orderResult.classList.remove("d-none");
    emptyResult.classList.add("d-none");

    orderIdInput.value = order.id;
}


/**
 * Crea un pedido.
 */
createOrderForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    const customerId =
        document.getElementById("customerId").value.trim();

    const productId =
        document.getElementById("productId").value.trim();

    const productName =
        document.getElementById("productName").value.trim();

    const unitPrice =
        Number(document.getElementById("unitPrice").value);

    const quantity =
        Number(document.getElementById("quantity").value);


    const request = {

        customerId: customerId,

        items: [
            {
                productId: productId,
                productName: productName,
                unitPrice: unitPrice,
                quantity: quantity
            }
        ]

    };


    try {

        const order = await apiRequest(API_URL, {
            method: "POST",
            body: JSON.stringify(request)
        });

        displayOrder(order);

        showAlert(
            "Pedido creado correctamente.",
            "success"
        );

    } catch (error) {

        showAlert(
            error.message,
            "danger"
        );
    }

});


/**
 * Consulta un pedido.
 */
getOrderButton.addEventListener("click", async () => {

    const orderId = orderIdInput.value.trim();

    if (!orderId) {

        showAlert(
            "Ingresa el ID del pedido.",
            "warning"
        );

        return;
    }


    try {

        const order = await apiRequest(
            `${API_URL}/${orderId}`
        );

        displayOrder(order);

        showAlert(
            "Pedido consultado correctamente.",
            "success"
        );

    } catch (error) {

        showAlert(
            error.message,
            "danger"
        );
    }

});


/**
 * Confirma un pedido.
 */
confirmOrderButton.addEventListener("click", async () => {

    const orderId = orderIdInput.value.trim();

    if (!orderId) {

        showAlert(
            "Ingresa el ID del pedido.",
            "warning"
        );

        return;
    }


    try {

        const order = await apiRequest(
            `${API_URL}/${orderId}/confirm`,
            {
                method: "POST"
            }
        );

        displayOrder(order);

        showAlert(
            "Pedido confirmado correctamente.",
            "success"
        );

    } catch (error) {

        showAlert(
            error.message,
            "danger"
        );
    }

});


/**
 * Cancela un pedido.
 */
cancelOrderButton.addEventListener("click", async () => {

    const orderId = orderIdInput.value.trim();

    if (!orderId) {

        showAlert(
            "Ingresa el ID del pedido.",
            "warning"
        );

        return;
    }


    try {

        const order = await apiRequest(
            `${API_URL}/${orderId}/cancel`,
            {
                method: "POST"
            }
        );

        displayOrder(order);

        showAlert(
            "Pedido cancelado correctamente.",
            "success"
        );

    } catch (error) {
      console.error("Error al cancelar pedido:", error);
        showAlert(
            error.message,
            "danger"
        );
    }

});

