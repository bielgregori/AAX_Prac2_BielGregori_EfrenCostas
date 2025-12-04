window.onload = init;

//DOM elements

const showFormButton = document.querySelector('.addDevice .button a');
const addButton = document.querySelector('#add_button');
const cancelButton = document.querySelector('#cancel_button');
const addDeviceForm = document.querySelector('.addDeviceForm');
const content = document.querySelector('.content');


//FUNCTIONS

function init() {	
	document.querySelector('.addDeviceForm').style.display = 'none';
}


function createDeviceElement(device) {
	
    const deviceDiv = document.createElement("div");
    deviceDiv.setAttribute("id", device.id);
    deviceDiv.setAttribute("class", "device " + device.type);

    const deviceName = document.createElement("span");
    deviceName.setAttribute("class", "deviceName");
    deviceName.innerHTML = device.name;
    deviceDiv.appendChild(deviceName);

    const deviceType = document.createElement("span");
    deviceType.innerHTML = "<b>Type:</b> " + device.type;
    deviceDiv.appendChild(deviceType);

    const deviceStatus = document.createElement("span");
    if (device.status === "On") {
        deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" id="+ device.id +" data-op=\"toggle\">Turn off</a>)";
    } else if (device.status === "Off") {
        deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" id="+ device.id +" data-op=\"toggle\">Turn on</a>)";
    }
    deviceDiv.appendChild(deviceStatus);

    const deviceDescription = document.createElement("span");
    deviceDescription.innerHTML = "<b>Comments:</b> " + device.description;
    deviceDiv.appendChild(deviceDescription);

    const removeDevice = document.createElement("span");
    removeDevice.setAttribute("class", "removeDevice");
	removeDevice.innerHTML = "<a href=\"#\" id="+ device.id +" data-op=\"remove\">Remove device</a>";
    deviceDiv.appendChild(removeDevice);

    return deviceDiv;
}


function onMessage(event) {
    const device = JSON.parse(event.data);
    if (device.action === "add") {
        const newDeviceElement = createDeviceElement(device);
        content.appendChild(newDeviceElement);
    }
    if (device.action === "remove") {
        document.getElementById(device.id).remove();
    }
    if (device.action === "toggle") {
        const node = document.getElementById(device.id);
        const statusText = node.children[2];
        if (device.status === "On") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" id="+ device.id +" data-op=\"toggle\">Turn off</a>)";
        } else if (device.status === "Off") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" id="+ device.id +" data-op=\"toggle\">Turn on</a>)";
        }
    }
}



//HANDLERS

function handleShowFormButton() {
	addDeviceForm.style.display = '';
}

function handleAddButton() {
    const name = addDeviceForm.querySelector('#device_name').value;
	const type = addDeviceForm.querySelector('#device_type').value;
	const description = addDeviceForm.querySelector('#device_description').value;

	addDeviceForm.style.display = 'none';
    addDeviceForm.reset();
    
	const DeviceAction = {
    	action: "add",
        name: name,
        type: type,
        description: description
    };
    socket.send(JSON.stringify(DeviceAction));
}


function handleCancelButton() {
	addDeviceForm.style.display = 'none';
    addDeviceForm.reset();
}


//LISTENERS

const socket = new WebSocket("ws://localhost:8080/websocketexample/actions");
socket.onmessage = onMessage;

showFormButton.addEventListener('click', handleShowFormButton);
addButton.addEventListener('click', handleAddButton);
cancelButton.addEventListener('click', handleCancelButton);

content.addEventListener('click', e => {
	
	if(e.target.getAttribute('data-op') === 'remove') {
    	const DeviceAction = {
        	action: "remove",
        	id: parseInt(e.target.id)
    	};
    	socket.send(JSON.stringify(DeviceAction));
    }

	if(e.target.getAttribute('data-op') === 'toggle') {
    	const DeviceAction = {
        	action: "toggle",
        	id: parseInt(e.target.id)
    	};
    	socket.send(JSON.stringify(DeviceAction));
    }
});
