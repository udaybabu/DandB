<%@page import="in.otpl.dnb.util.ConfigManager"%>
<script src="https://maps.googleapis.com/maps/api/js?key=<%=ConfigManager.googleMapKey%>&callback=initializeMap" async defer></script>
<script type="text/javascript">
var map;
var markers = [];

// Clear Map
function clearOverlaysMap() {
	for (var i = 0; i < markers.length; i++ ) {
		markers[i].setMap(null);
	}
	markers.length = 0;
}

// Auto zoom
function AutoCenterMap() {
    var bounds = new google.maps.LatLngBounds(); //  Create a new viewpoint bound
    $.each(markers, function (index, marker) { //  Go through each...
      bounds.extend(marker.position);
    });
    map.fitBounds(bounds); //  Fit these bounds to the map
}
</script>