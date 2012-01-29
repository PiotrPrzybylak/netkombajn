package pl.netolution.sklep3.web.controller.composite;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import pl.netolution.sklep3.dao.CompositeProductDao;
import pl.netolution.sklep3.dao.ProductDao;
import pl.netolution.sklep3.domain.CompositeProduct;
import pl.netolution.sklep3.domain.Price;
import pl.netolution.sklep3.domain.Product;

public class CompositeAjaxController extends AbstractController {

	private ProductDao productDao;

	private CompositeProductDao compositeProductDao;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintWriter writer = response.getWriter();

		long[] productIds = ServletRequestUtils.getLongParameters(request, "productIds");
		long compositeId = ServletRequestUtils.getLongParameter(request, "compositeId");

		Price compositePrice = new Price();

		for (long productId : productIds) {
			Product product = productDao.findById(productId);
			compositePrice = compositePrice.add(product.getRetailGrossPrice());
		}

		CompositeProduct compositeProduct = compositeProductDao.findById(compositeId);

		compositePrice.changeByPercentage(compositeProduct.getProfitPercent());
		//TODO use price.tag
		writer.write(compositePrice.toString() + "&nbsp;PLN");

		writer.flush();
		writer.close();

		return null;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setCompositeProductDao(CompositeProductDao compositeProductDao) {
		this.compositeProductDao = compositeProductDao;
	}

}
