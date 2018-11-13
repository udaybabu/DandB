$(document).ready(function() {

var calendar =	flatpickr("#dateFrom", {
		enableTime : true,
		altInput : true,
		//maxDate : "today",
		altFormat : "d-M-Y H:i",
		//defaultDate:"today"
	});
	
	var toCalendar =flatpickr("#dateTo", {
		enableTime : true,
		altInput : true,
		//maxDate : "today",
		altFormat : "d-M-Y H:i",
		//defaultDate:"today"
	});
	
	
	$(".from-addon").on("click", function(){	
		calendar.open();
					});
					
	$(".to-addon").on("click", function(){	
		toCalendar.open();
					});
});