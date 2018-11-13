<jsp:include page="../common/googleMapApi3.jsp"></jsp:include>
<script>
var poly;
var loadCompleteMap = false;
var latlongArray = [];
var LocationData = [];
var markerLat = [];
var markerLong = [];
var firstPoint = true;
var lastPoint  = false;
var zoomcheck=true;
function showMap(fieldName) {
    var checkBoxElement = fieldName;
    var atleastOneChecked = false;
    var flag = false;
    var noValidData = false;
  
	if(!checkBoxElement.length){
		checkBoxElement = [checkBoxElement];
	}
	
	for (var i = 1; i <= checkBoxElement.length ; i++) {
  		var data = "";
  		$("span#checkCounter_"+i).html(data);
  	}
  	var checkCounter = 0;
  	
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
                var d = stringArray[0]+"," +stringArray[1];
                markerLat.push(stringArray[0]);
   			 markerLong.push(stringArray[1]);
                LocationData.push(d);
            }
        }
    }
// for loop close
   
    if (LocationData.length > 0) {
        	initializeMap();
        	$('#myModal').modal('show');
        } 
}   
      function initializeMap(){
    	  clearOverlaysMap(); // Clear Map Overlays
    	  console.log(LocationData.length);
    	  var initlatlng = new google.maps.LatLng(markerLat,markerLong);
  	    var mapOptions = {
  	    	center: new google.maps.LatLng(12.9715987,77.5945627),
  	    	zoom: 14,
  	        mapTypeId: google.maps.MapTypeId.ROADMAP
  	    };
  	    map = new google.maps.Map(document.getElementById('map'), mapOptions);
  	    var marker, i;
  	    var latlngbounds = new google.maps.LatLngBounds();
  	    
	        for (var k = 0; k < LocationData.length; k++) {
	        	
	            if(k == LocationData.length - 1) {            
	            	lastPoint = true;
	            }            
	            var dataValue = LocationData[k];
	            var dataArray = dataValue.split(",");
	            console.log("data="+dataArray);
	            var dir = k*1+1;
	            
	            var markerOptions;
	            if(document.getElementById('polyline').value == 1){
		            if (firstPoint == true) {
		            	markerOptions = {
			            		url: "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/green-dot.png"
			            };
		                
		                firstPoint = false;                		            
		            } else if(lastPoint){//alert("last");
		            	markerOptions = {
			            		url: "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/red-dot.png"
			            };
		            	
		            	lastPoint == false;            	
		            }else{            	
		            	markerOptions = {
		            		url: "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/blue-dot.png"
		            	};
		            } 
	            }else{
	            	markerOptions = {
		            	url: "http://www.google.com/intl/en_ALL/mapfiles/ms/micons/blue-dot.png"
		            };
	            }
	            var point=new google.maps.LatLng(dataArray[0], dataArray[1]);
	            marker = new google.maps.Marker({
	            	position: new google.maps.LatLng(dataArray[0], dataArray[1]),
	                draggable: false,
	    			raiseOnDrag: false,
	    			icon:markerOptions,
	    			map: map
	                        });
	            markers.push(marker);
	            latlongArray.push(point);
	          
	            var infowindow = new google.maps.InfoWindow({
	            	
	            });
	           
	            google.maps.event.addListener(marker, 'click', function() {
	        		
	        	});
	           
	        	latlngbounds.extend(marker.position);
	        }
	        var bounds = new google.maps.LatLngBounds();
	       
	        //Center map and adjust Zoom based on the position of all markers.
	       this.map.setCenter(latlngbounds.getCenter());
	        this.map.fitBounds(latlngbounds);
	        if(zoomcheck){
	        	google.maps.event.addListenerOnce(map, 'idle', function(){
	        		AutoCenterMap();
	        	});
	        }
	        google.maps.event.addDomListener(window, 'load', initializeMap);
	        
	        if(document.getElementById('polyline').value == 1){
		        var polyOptions = {
		        		path: latlongArray,
		        	    geodesic: true,
		        	    strokeColor: '#FF0000',
		        	    strokeOpacity: 1.0,
		        	    strokeWeight: 3
		        	  };
		        poly = new google.maps.Polyline(polyOptions);
		        poly.setMap(map);
	        }
	        
            loadCompleteMap = false;
    	      
      }

	    </script>
	    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	    <div class="modal-dialog" role="document">
	    <div class="modal-content">
	    <div class="modal-header">
	    <h5 class="modal-title" id="exampleModalLabel">Attendance Report</h5>
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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
	    $("#myModal").on("shown.bs.modal", function () {
	        google.maps.event.trigger(map, "resize");
	    }); 
	    </script>