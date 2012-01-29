<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag body-content="empty"  %>

<%@ attribute name="shipmentOption" required="true" type="pl.netolution.sklep3.domain.ShipmentOption" %>
<%@ attribute name="totalWeight" type="java.lang.Double" %>

<%=shipmentOption.getPriceForWeight(totalWeight)%> 

