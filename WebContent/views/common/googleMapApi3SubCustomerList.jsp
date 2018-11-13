<jsp:include page="../common/googleMapApi3.jsp"></jsp:include>
<script>
var loadCompleteMap = false;
var latlongArray = [];
var LocationData = [];
var markerLat = [];
var markerLong = [];
var markerLabel = [];
var name = [];
//var distance = [];
//var time = [];
var zoomcheck=true;
var markerOptions;
var mapOptions;
function showMap(fieldName) {
	markerLat=[];
	markerLong=[];
	LocationData=[];
	latlongArray=[];
	markerLabel=[];
	name=[];
//	distance = [];
//	time = [];
	var checkBoxElement = fieldName;
    var atleastOneChecked = false;
    var flag = false;
    var noValidData = false;
    var checkCounter = 0;
	if(!checkBoxElement.length){
		checkBoxElement = [checkBoxElement];
	}
	for (var i = 1; i <= checkBoxElement.length ; i++) {
  		var data = "";
  		$("span#checkCounter_"+i).html(data);
  	}
    for (var i = checkBoxElement.length - 1 ; i >= 0 ; i--) {
        if (checkBoxElement[i].checked) {
        	var j = i+1;
            atleastOneChecked = true;
            noValidData = true;
            var val = checkBoxElement[i].value;
            var stringArray = val.split(",");
            if (isNaN(stringArray[0] * 1) || isNaN(stringArray[1] * 1)) {
                continue;
            } else {
	    		checkCounter = checkCounter + 1;
	        	$("span#checkCounter_"+j).html(checkCounter);
                var d =  stringArray[0] + "," + stringArray[1]+","+stringArray[2];
                markerLat.push(stringArray[0]);
   				markerLong.push(stringArray[1]);
   				//name.push(stringArray[2]);
   				//alert(name.push(stringArray[2]));
   				//distance.push(stringArray[3]);
   				//time.push(stringArray[4]);
                LocationData.push(d);
                markerLabel.push(""+checkCounter);
            }
        }
    }
   if (LocationData.length > 0) {
        initializeMap();
        $('#myModal').modal('show');
      } 
   else
	   {
	   bootbox.alert('Please select record(s) to display on Map');
	   }
  }   
function initializeMap(){
   clearOverlaysMap(); // Clear Map Overlays
   console.log(LocationData.length);
   var initlatlng = new google.maps.LatLng(markerLat,markerLong);
   mapOptions = {
        center: new google.maps.LatLng(12.9715987,77.5945627),
   		zoom: 5,
  		mapTypeId: google.maps.MapTypeId.ROADMAP
  	  };
   map = new google.maps.Map(document.getElementById('map1'), mapOptions);
   var marker, i;
   var latlngbounds = new google.maps.LatLngBounds();
   for (var k = 0; k < LocationData.length; k++) {
	    markerOptions="";
	    mapOptions="";
	    if(k == LocationData.length - 1) {            
	    lastPoint = true;
	    }            
	   var dataValue = LocationData[k];
	   var dataArray = dataValue.split(",");
	   var infoContent = name[k];
	// var point=new google.maps.LatLng(dataArray[0], dataArray[1]);
	 marker = new google.maps.Marker({
	 	  position: new google.maps.LatLng(dataArray[0], dataArray[1]),
		  draggable: false,
	 	  raiseOnDrag: false,
	 	  icon: markerOptions,
	 	 title: name[k],
	 	 map: map,
	 	labelAnchor: new google.maps.Point(75, 50), //(25, 27),
		labelClass: "my_label", // the CSS class for the label
		labelInBackground: true,
		clickable: true,
		 label : {
				text : infoContent,
				color : "#ac5353",
				fontSize : '12px',
				fontFamily : '"Courier New", Courier,Monospace',
				fontWeight : "bold",
				border: "5px solid red"
			} 
	 	 
	  });
	markers.push(marker);
	/* html = '<div class="panel panel-default" style="width: 300px">\
		<div class="row-fluid">\
        <div class="span6">'+infoContent+'</div>\
    </div>\
    </div>';  */
	
	 infowindow = new google.maps.InfoWindow();
		 
	//slatlongArray.push(point);
	google.maps.event.addListener(marker, 'click', function() {
		infoContent.setContent(name[k]);
		infowindow.open(map, marker);
	});
	latlngbounds.extend(marker.position);
}
var bounds = new google.maps.LatLngBounds();
//Center map and adjust Zoom based on the position of all markers.
map.setCenter(latlngbounds.getCenter());
map.fitBounds(latlngbounds);
if (zoomcheck) {
	google.maps.event.addListenerOnce(map, 'idle', function() {
		AutoCenterMap();
	});
}
google.maps.event.addDomListener(window, 'load', initializeMap);
}
</script>
	    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    <div class="modal-body">
	    <div id="map1" style="width: 100%; height: 400px; margin-bottom: 15px;"></div>
	    </div>
	    <div class="modal-footer">
	    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
	    </div>
	    </div>
	    </div>
	    </div>
	    <script>
	    $("#myModal").on("shown.bs.modal", function () {
	        google.maps.event.trigger(map, "resize");
	    }); 
	    </script>