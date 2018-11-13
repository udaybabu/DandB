<%@page import="in.otpl.dnb.util.SessionConstants"%>
<%@page import="in.otpl.dnb.util.URLConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="in.otpl.dnb.util.ServerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<style>
.fmodal{
 z-index: 9999 !important; 
}
 
#map {
	margin: 0px;
	padding: 0px
}
textarea {
   resize: none;
}
.labels {
	color: red;
	background-color: white;
	font-family: "Lucida Grande", "Arial", sans-serif;
	font-size: 10px;
	font-weight: bold;
	text-align: center;
	width: auto;
	border: 1px solid black;
	white-space: nowrap;
}

  .map-marker-label{
position: absolute;
border: 1px solid #eb3a44;
  border-radius: 2px;
  background: #fee1d7;
  text-align: center;
  line-height: 10px;
  font-weight: 300;
  font-size: 12px;
  padding:5px;
  color: #333;
}

#mtime{
font-size:12px;
}
.fc-center h2{
font-size:16px;
margin-top:5px;
}
</style>
<div class="container-fluid">
<div class="row">
<div class="col-lg-12 col-md-12 mapStyle" style="margin-right:10px;">
<div class="panel panel-default">
<div class="panel-heading">
All User Map
</div>
<div class="panel-body">
<div id="map" style="width:100%;height:400px;margin-bottom:15px;"></div>
<h6>Note: Last Known Location</h6>
</div>
</div>
</div>
</div>
</div>
<jsp:include page="common/googleMapApi3.jsp"></jsp:include>
<script type="text/javascript">
var markerSize = {
	x: 22,
	y: 40
	  };
function initializeMap() {
	clearOverlaysMap(); // Clear Map Overlays
	var zoomcheck=true;
var markerLat = [];
var markerLong = [];
var markerName = [];
var markerTime = [];
<c:forEach var="aumd" items="${allUserMapData}" > 
	markerName.push("${aumd.name}");
	markerLat.push("${aumd.lattitude}");
	markerLong.push("${aumd.longitude}");
	markerTime.push("${aumd.modificationTime}");
</c:forEach>
	  google.maps.Marker.prototype.setLabel = function(label) {
	this.label = new MarkerLabel({
	  map: this.map,
	  marker: this,
	  text: label
	});
	this.label.bindTo('position', this, 'position');
	  };

	  var MarkerLabel = function(options) {
	this.setValues(options);
	this.span = document.createElement('span');
	this.span.className = 'map-marker-label';
	  };

	  MarkerLabel.prototype = $.extend(new google.maps.OverlayView(), {
	  onAdd: function() {
	  this.getPanes().overlayImage.appendChild(this.span);
	  var self = this;
	  this.listeners = [
	google.maps.event.addListener(this, 'position_changed', function() {
	  self.draw();
	})
	  ];
	},
	draw: function() {
	  var text = String(this.get('text'));
	  var position = this.getProjection().fromLatLngToDivPixel(this.get('position'));
	  this.span.innerHTML = text;
	  this.span.style.left = (position.x - (markerSize.x / 2)) - (text.length * 3) + 10 + 'px';
	  this.span.style.top = (position.y - markerSize.y + 40) + 'px';
	}
	  });
	var initlatlng = new google.maps.LatLng(markerLat,markerLong);
	var mapOptions = {
		center: new google.maps.LatLng(12.9715987,77.5945627),
		zoom: 28,
		mapTypeId: google.maps.MapTypeId.ROADMAP
		};
	map = new google.maps.Map(document.getElementById('map'), mapOptions);
	var marker, i;
	var latlngbounds = new google.maps.LatLngBounds();
	for (i = 0; i < markerLat.length; i++) {
		var name = markerName[i];
		var time = markerTime[i].split(".")[0];
    	marker = new google.maps.Marker({
		position: new google.maps.LatLng(markerLat[i], markerLong[i], name, time),
            draggable: false,
			raiseOnDrag: false,
			map: map, 
        	label: name +" - " + time
     });  
	markers.push(marker);
	var infoContent = name +" - "+ time;
	var infowindow = new google.maps.InfoWindow({
		content: infoContent
	});
  google.maps.event.addListener(marker, 'click', (function(marker, i) {
	 return function(){
		infowindow.setContent(markerName[i] + "-" +markerTime[i].split(".")[0]);
		infowindow.open(map, marker);
		}
	})(marker, i));
	latlngbounds.extend(marker.position);
	}
var bounds = new google.maps.LatLngBounds();

//Center map and adjust Zoom based on the position of all markers.
map.setCenter(latlngbounds.getCenter());
map.fitBounds(latlngbounds);
if(zoomcheck){
	google.maps.event.addListenerOnce(map, 'idle', function(){
		AutoCenterMap();
	});
}
google.maps.event.addDomListener(window, 'load', initializeMap);
}

</script>
