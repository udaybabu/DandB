//JavaScript Document
$(document).ready(function() {
	$("#spinner").fadeOut("slow");	
	
	//navigation
	$('.navbar-toggler').on('click', function(event) {
		event.preventDefault();
		$(this).closest('.navbar-minimal').toggleClass('open');
	});

	//tooltip
	$("body").tooltip({ selector: '[data-toggle=tooltip]' });
	

/*	//multiselect
	$('#team, #dis_team').multiselect({
		includeSelectAllOption: true,
		enableFiltering: true,
		buttonWidth: '100%',
		maxHeight: 300,
		dropRight: true	
	});*/

/*	//datePicker	
	const fpConf = {
			enableTime: true,
			altInput: true,
			altFormat : "d-M-Y H:i",
			"mode": "range",
			wrap: true,
			allowInput: true
	};
	var clone = $(".taskContent").clone();
	flatpickr(".input-group", fpConf);
	flatpickr(".taskContent .input-group", fpConf);
	//task
	$("#addTask").on("click", function(event){
		event.stopPropagation();
		const newClone = clone.clone();
		var del = '<h4 class="glyphicon glyphicon-trash deleteTask" style="color:#f44336;cursor:pointer"></h4>';
		newClone.find("h4").parent().append(del);
		$("#taskEmpty").append(newClone);
		$(newClone).find(".input-group").flatpickr(fpConf);
		$(".deleteTask").on("click", function(){
			$(this).parent().parent().parent().remove();
		});
	});*/

/*	//select all checkbox
	$("#checkAll").change(function () {
		$(".table tr td input:checkbox").prop('checked', $(this).prop("checked"));
	});	*/

	//map resize important for show map inside a modal box
	$("#mapModal").on("shown.bs.modal", function () {
		google.maps.event.trigger(map, "resize");
	});

	//hiding filter on load
	/*$(".filter").parent().hide();*/

	//hiding table on load
	/*$(".white").parent().hide();*/

/*	//Activity Report form submission
	$('#activityForm').on('submit', function(e){
		e.preventDefault();
		//hiding form
		$(".activityStyle").parent().hide();
		//show filter
		$(".filter").parent().show();
		//show table
		$(".white").parent().show();

	});

	//modify filter
	$("#modifyFilter").on("click", function(){
		$(".filter").parent().hide();
		$(".activityStyle").parent().show();
	});

	//task form submission
	$('#taskForm').on('submit', function(e){
		e.preventDefault();
		//hiding form
		$(".panel-default").parent().hide();
		//show filter
		$(".filter").parent().show();
		//show table
		$(".white").parent().show();

	});*/

/*	//addpackage
	$(".addPackage").on("click", function(){
		var clone = $(this).parent();
		var remove = '<h4 class="glyphicon glyphicon-minus-sign text-danger pull-right removePackage" style="cursor:pointer"></h4>';
		$(this).remove();
		clone.append(remove);
		$(".selectedPackage").append(clone);
		$(".removePackage").on("click", function(){
			var selection = $(this).parent();
			var add = '<h4 class="glyphicon glyphicon-plus-sign text-success pull-right addPackage" style="cursor:pointer">';
			$(this).remove();
			selection.append(add);
			$(".availablePackage").append(selection);
		});
	});
	//removepackage
	$(".removePackage").on("click", function(){
		var selection = $(this).parent();
		var add = '<h4 class="glyphicon glyphicon-plus-sign text-success pull-right addPackage" style="cursor:pointer">';
		$(this).remove();
		selection.append(add);
		$(".availablePackage").append(selection);
		$(".addPackage").on("click", function(){
			var clone = $(this).parent();
			var remove = '<h4 class="glyphicon glyphicon-minus-sign text-danger pull-right removePackage" style="cursor:pointer"></h4>';
			$(this).remove();
			clone.append(remove);
			$(".selectedPackage").append(clone);
		});
	});*/

});