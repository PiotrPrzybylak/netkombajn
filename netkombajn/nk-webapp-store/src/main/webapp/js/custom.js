var $j = jQuery.noConflict();
	   
$j(document).ready(function() {


		$j("#categories > .box-content > ul > li > .categoryToogleGadget").click(function(event){
		   $j(this).parent().children("ul").children("li").toggle();
		   });

		displayInvoiceDataConditionally(null);
		$j("#invoiceCheckbox").click(displayInvoiceDataConditionally);

});



function displayInvoiceDataConditionally(event){
		if ($j('#invoiceCheckbox:checked').val() != null) {
			$j("#invoiceData").show();
		} else {
			$j("#invoiceData").hide();
		}
}