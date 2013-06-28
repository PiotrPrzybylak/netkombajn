CompositeProductDisplay=
{
		
		
		changeVisibility:function (blockToHideId,blockToShowId,groupName){
		
			var blockToHideIdLiteral='#'+blockToHideId;
			var blockToShowIdLiteral='#'+blockToShowId;
	
			$(blockToHideIdLiteral).addClass("hidden");
			$(blockToHideIdLiteral).removeClass("compositeProductPart");
			
			$(blockToShowIdLiteral).removeClass("hidden");
			$(blockToShowIdLiteral).addClass("compositeProductPart");
			
			var newProductId=$(blockToShowIdLiteral).children('#productId').text();
			
			//grupName to nazwa ukrytego pola
			//ktore przekaze id produktu do zamowienia
			var groupField= $('#'+groupName);
			groupField.val(newProductId);
		
			//dane przekazywane do funkcji poprzez ajax
			var controllerData=new Array();
	
			$(".groupMarker").each(function(index,element){
				controllerData[index]=element.value;
			})
					
			var compositeId=$("#compositeProductId").val();
			$("#compositePriceValue").load("compositeAjax.html",{'productIds':controllerData,"compositeId":compositeId});
			
		}
}



