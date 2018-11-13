<jsp:include page="../common/googleMapApi3.jsp"></jsp:include>
<script>
var infoWindow,mapOptions;
var lat="";
var lon="";
var markerOptions = {url : "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/red-dot.png"};
function myMap(){
	 lat=$("#lat").val();
    lon=$("#lon").val(); 
	initializeMap();
}
function initializeMap(){
	var mapCanvas = document.getElementById("map");
	mapOptions = {
			center: new google.maps.LatLng(12.9716, 77.5946),
			zoom: 14,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(mapCanvas, mapOptions);
	var infoWindow = new google.maps.InfoWindow;
	 if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) { 
	    	var pos = {
	        	lat: position.coords.latitude,
	            lng: position.coords.longitude
            };
	    	var marker = new google.maps.Marker({
	    		position : pos,
				map : map,
				icon : markerOptions
			});
			markers.push(marker);
	     }, function() {
	        handleLocationError(true, map.getCenter());
        });
	} else {// Browser doesn't support Geolocation
		handleLocationError(false,map.getCenter());
	} 
	google.maps.event.addListener(map, 'click', function(e) {
		clearOverlaysMap();
		$("#lat").val(e.latLng.lat());
		$("#lon").val(e.latLng.lng());
		var point = new google.maps.LatLng(e.latLng.lat(),e.latLng.lng());
		var marker = new google.maps.Marker({
			position : point,
			map : map,
			icon : markerOptions
		});
		markers.push(marker);
	});
	 function handleLocationError(browserHasGeolocation, pos) {
		infoWindow.setPosition(pos);
	    infoWindow.setContent(browserHasGeolocation ? 'Error: The Geolocation service failed.' :'Error: Your browser doesn\'t support geolocation.');
	    infoWindow.open(map);
	} 
}
 
/* function initializeMap(addr) {
	var geocoder = new google.maps.Geocoder();
	var geocoderOptions = {
		address : addr,
		region : 'yes'
	}
	geocoder.geocode(geocoderOptions,function(results, status) {
		if (status.toLowerCase() == 'ok') {
			var coords = new google.maps.LatLng(results[0]['geometry']['location'].lat(),results[0]['geometry']['location'].lng());
			var lat = coords.lat();
			var lng = coords.lng();
			var mapOptions = {
				zoom : 14,
				center : new google.maps.LatLng(lat, lng),
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			this.map = new google.maps.Map(document.getElementById("map"), mapOptions);
			var point = new google.maps.LatLng(lat, lng);
			var marker = new google.maps.Marker({
				position : point,
				map : map,
				icon : markerOptions
			});
			markers.push(marker);
			google.maps.event.addListener(map,'click',function(e){
				clearOverlaysMap();
				if(e.latLng){
					$("#lat").val(e.latLng.lat());
					$("#lon").val(e.latLng.lng());
					var point = new google.maps.LatLng(e.latLng.lat(),e.latLng.lng());
					var marker = new google.maps.Marker({
						position : point,
						map : map,
						icon : markerOptions
					});
					markers.push(marker);
				}
			});
		}   else {
			bootbox.alert('<spring:message code="landmark.label.address"/>');
			return false;
		} 
	});
}

function initializeMapEdit(landmarkName, lat, lon,radius) {
	var lat=Math.round(lat*100000)/100000;
	var lon=Math.round(lon*100000)/100000;
	var marker;
	var zoomcheck = true;
	var mapOptions = {
 		    center: new google.maps.LatLng(lat, lon),
 		   zoom : 7,
 		    mapTypeId: google.maps.MapTypeId.ROADMAP
 	};
	this.map = new google.maps.Map(document.getElementById("map"), mapOptions);
	 
	var point = new google.maps.LatLng(lat, lon);
     var marker = new google.maps.Marker({
        map: map,
        position: point,
		icon : markerOptions
    });  
    var infoContent = "Landmark : "+landmarkName+", Radius : "+radius;
    var latlngbounds = new google.maps.LatLngBounds();
    marker = new google.maps.Marker({
		position : point,
		draggable : false,
		raiseOnDrag : false,
		map : map,
		icon : markerOptions,
		title : "Landmark : "+landmarkName+", Radius : "+radius,
		labelAnchor : new google.maps.Point(25, 27),
		label : {
			text : "Landmark : "+landmarkName+", Radius : "+radius,
			color : "blue",
			fontSize : '10px',
			fontFamily : '"Courier New", Courier,Monospace',
			fontWeight : "bold"
		}
	});
	 markers.push(marker);
        var infoContent = name;
        var infowindow = new google.maps.InfoWindow({
        	content: infoContent
        });
        
    var infowindow = new google.maps.InfoWindow({
        content: infoContent
    });
    google.maps.event.addListener(map, 'click', function(e) {
    	document.getElementById("lat").value = e.latLng.lat();
		document.getElementById("lon").value = e.latLng.lng();
		clearOverlaysMap();
		if(e.latLng){
			$("#lat").val(e.latLng.lat());
			$("#lon").val(e.latLng.lng());
			var point = new google.maps.LatLng(e.latLng.lat(),e.latLng.lng());
			var marker = new google.maps.Marker({
				position : point,
				map : map,
				icon : markerOptions
			});
			markers.push(marker);
		}
    });
    latlngbounds.extend(marker.position);
   
    //Center map and adjust Zoom based on the position of all markers.
   map.setCenter(latlngbounds.getCenter());
   map.fitBounds(latlngbounds);
   AutoCenterMap();
    if(zoomcheck){
	     google.maps.event.addListenerOnce(map, 'idle', function(){
	     AutoCenterMap();
	    });
	 }
	 google.maps.event.addDomListener(window, 'load', initializeMap);
     
    var polyOptions = {
    	center: point,
       	radius: radius*1,
       	strokeColor: "#f33f00",
      	strokeOpacity: 1.0,
       	strokeWeight: 3,
       	fillColor: "#FF0000",
       	fillOpacity: 0.2
    }
    var poly = new google.maps.Circle(polyOptions);
	poly.setMap(map);
}
	
function showmap() {
	var addr = $("#address").val();
	if (addr == "")
		bootbox.alert('Address should not be Empty ');
	else {
		initializeMap(addr);
	}
}

function showMapEdit() {
	var landmarkName = document.getElementById('name').value;
	var lat = document.getElementById('lat').value;
	var lon = document.getElementById('lon').value;
	var radius= document.getElementById('radius').value;
	initializeMapEdit(landmarkName, lat, lon, radius);
	$('#myModal').modal('show');
}  */
	
</script>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Locate on Map</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-lg-2 col-md-2">
						<div class="form-group">
							<span>Address</span>
						</div>
					</div>
					<div class="col-lg-4 col-md-4">
						<div class="form-group">
							<input type="text" id="address">
						</div>
					</div>
					<div class="col-lg-4 col-md-4">
						<div class="form-group">
							<button type="button" class="btn btn- btn-primary" id="submit" style="text-align: center;" onclick="javascript:showmap()">Search</button>
						</div>
					</div>
				</div>

				<div id="map" style="width: 100%; height: 400px; margin-bottom: 15px;"></div></div>
			<div class="modal-footer"><button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button></div>
		</div>
	</div>
</div>
<script>
$("#myModal").on("shown.bs.modal", function () {
    google.maps.event.trigger(map, "resize");
});
</script>