"use strict";

var _responses = {};

function loadStatus() {
	getJSON('GET', 'rest/status', showStatus, errorFunc);
}

function showStatus(data) {
	_responses.status = data;
	var enabled = data.enabled;
	var enabledField = document.getElementById('enabled');
	enabledField.innerText = enabled ? 'YES' : 'NO';
	enabledField.classList.value = enabledField.innerText.toLowerCase();
}

function hello(evtData) {
	if (!evtData) {
		console.log('no param!')
		return
	}
	if (!evtData.target) {
		_responses.hello = evtData;
		console.log("hello data:",_responses.hello);
		var helloDiv = document.getElementById('hello');
		helloDiv.innerText = _responses.hello.name;
		return
	}
	var name = document.getElementById('name').value;
	getJSON('GET', 'rest/hello?name='+name, hello, errorFunc);
}
	
function helloDone(data, evt) {
	_responses.hello = data;
	console.log("hello data:",data);
	var helloDiv = document.getElementById('hello');
	helloDiv.innerText = data.name;
}

function getString(ordinal) {
	// from https://stackoverflow.com/a/12502559/3889
	return ordinal+'_'+Math.random().toString(36).slice(2);
}

function stuff(evt) {
	var resource = getString(1) + "/" + getString(2) + "/" + getString(3);
	getJSON('post', '/rest/stuff/'+resource, stuffDone, errorFunc);
}

function stuffDone(data, evt) {
	console.log(data);
	_responses.stuff = data;
	data.sort(compareStatus);
	var body = document.getElementById('stuff-body');
    body.innerHTML = '';
	data.forEach(function(d) {
		var row = '<tr>';
		row += td(d.name);
		row += td(d.value);
		row += td(d.enabled ? "Yes" : "No");
		row += '</tr>';
		body.insertAdjacentHTML('beforeend', row);
	});
}

function getJSON(httpMethod, url, successFunc, failFunc, body) {
	var xhr = new XMLHttpRequest();
	xhr.onload = function(evt) {
	    if (evt.target && evt.target.status === 200) {
	        var info = JSON.parse(evt.target.responseText);
	        successFunc(info, evt);
	    }
	    else {
	    	if (failFunc)
	    		failFunc(evt);
	    	return null;
	    }
	};
	if (failFunc)
		xhr.addEventListener("error", failFunc);
	// xhr.addEventListener("abort", transferCanceled);

	xhr.open(httpMethod, url);
	xhr.setRequestHeader('Content-Type', 'application/json');
	// xhr.send(JSON.stringify(body));
	xhr.send();
}

function td(v, cls) {
	// we want the auto-conversion from null obj to "null" string
	return '<td' + (cls?'class='+cls:'') + '>' + (v && v != 'null' ? v : '') + '</td>';
}

function errorFunc(evt) {
	var msg = 'Call failed';
	if (evt.target && evt.target.responseText) {
		var xhr = JSON.parse(evt.target.responseText);
		msg = 'Call to ' + xhr.path + ' failed: "' 
		  + (xhr.message ? xhr.message : xhr.error)
		  + '"';
	}
	alert(msg);
	console.log(msg);
	console.log(evt);
}

function cmp(a, b) {
	// a) convert boolean to string and b) sort nulls to end
	var tmpa = a ? a+"" : "zzzz"; 
	var tmpb = b ? b+"" : "zzzz";
	if (tmpa > tmpb)
		return 1;
	else if (tmpa < tmpb)
		return -1;
	else
		return 0;
}

function compareStatus(a, b) {
	var res;
    if ((res = cmp(a.name, b.name)) != 0) 
        return res;
    if ((res = cmp(a.value, b.value)) != 0) 
        return res;
    if ((res = cmp(a.enabled, b.enabled)) != 0) 
        return res;
    return 0;    
}

