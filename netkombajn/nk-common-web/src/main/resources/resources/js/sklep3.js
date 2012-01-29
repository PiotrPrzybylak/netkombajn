$j(document).ready(function() {


	   var var_name = $j("input[name='order.customer.customerType']:checked").val();

	  if (var_name == 'COMPANY') {
	   		$j("#companyNameInputField").show();
	   		$j("#nipInputRequirement").show();		   
	  }
	   
	   
	   $j("#companyCustmerTypeButton").click(function(event){
		   		$j("#companyNameInputField").show();
		   		$j("#nipInputRequirement").show();		   		
		   });

	   $j("#personCustmerTypeButton").click(function(event){
	   		$j("#companyNameInputField").hide();
	   		$j("#nipInputRequirement").hide();		   		
	   });
	
});
