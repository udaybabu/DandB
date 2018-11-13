<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="googleMapApi3.jsp"></jsp:include>
<script type="text/javascript">
	var checkBoxUser = new Object();
	var chackboxArray = new Array();
	function getCheckValue(id) {
		var key = id;
		var result = checkBoxUser[key];
		if (result == undefined) {
			return true;
		}
		return result
	}

	function checkUncheck(id, checkVal) {
		var key = id;
		checkBoxUser[key] = checkVal;
	}

	function checkAll(value) {
		field = document.forms[0].mapData;
		if (field) {
			if (!field.length)
				field = [ field ];
			for (i = 0; i < field.length; i++)
				field[i].checked = value;
		}
	}
	function checkTeamAll(value, teamName, size) {
		for (i = 0; i < size; i++) {
			document.getElementById(teamName + '_' + i).checked = value;
		}
	}
	function getUserIconColor(colorStatus) {
		var colorIcon = "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/blue-dot.png";
		if (colorStatus == 'blue') {
			return colorIcon;
		} else if (colorStatus == 'green') {
			colorIcon = "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/green-dot.png";
		} else if (colorStatus == 'yellow') {
			colorIcon = "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/yellow-dot.png";
		} else if (colorStatus == 'orange') {
			colorIcon = "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/orange-dot.png";
		} else if (colorStatus == 'red') {
			colorIcon = "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/red-dot.png";
		}
		return colorIcon;
	}
	var val = [];
	var markerLat = [];
	var markerLong = [];
	var names = [];
	var teamNames = [];
	var time = [];
	var color = [];
	function myMap() {
		val = [];
		markerLat = [];
		markerLong = [];
		names = [];
		teamNames = [];
		time = [];
		color = [];
		$('.actions :checkbox:checked').each(function(i) {
			val[i] = $(this).val();
			
		});
		if (val.length > 0) {
			for (var i = 0; i < val.length; i++) {
				var stringArray = val[i].split(",");
				names.push(stringArray[0]);
				markerLat.push(stringArray[1]);
				markerLong.push(stringArray[2]);
				time.push(stringArray[4]);
				teamNames.push(stringArray[5]);
				color.push(stringArray[6]);
			}
			initializeMap();
			$('#myModal').modal('show');
		}else{
			bootbox.alert('Please Select CheckBox to display map');
		}
	}
	function initializeMap() {
		var markerOptions;
		clearOverlaysMap(); // Clear Map Overlays	
		var zoomcheck = true;
		var initlatlng = new google.maps.LatLng(markerLat, markerLong);
		var mapOptions = {
			center : new google.maps.LatLng(12.9715987, 77.5945627),
			zoom : 14,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById('map'), mapOptions);
		var marker, i;
		var latlngbounds = new google.maps.LatLngBounds();
		for (i = 0; i < markerLat.length; i++) {
			var name = names[i] + " (" + teamNames[i] + "): " + time[i];
			var iconType = getUserIconColor(color[i]);
			var markerOptions = {
				url : iconType
			};
			marker = new google.maps.Marker({
				position : new google.maps.LatLng(markerLat[i], markerLong[i], name),
				draggable : false,
				raiseOnDrag : false,
				map : map,
				icon : markerOptions,
				title : name,
				labelAnchor : new google.maps.Point(25, 27),
				label : {
					text : name,
					color : "red",
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
		        google.maps.event.addListener(marker, 'click', (function(marker, i) {
		        	return function(){
						infowindow.setContent(names[i] + " :" + time[i] + ":" + teamNames[i]);
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
				<div id="map" style="width: 100%; height: 400px; margin-bottom: 15px;"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script>
	$("#myModal").on("shown.bs.modal", function() {
		google.maps.event.trigger(map, "resize");
	});
</script>