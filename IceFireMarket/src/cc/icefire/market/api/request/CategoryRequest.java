package cc.icefire.market.api.request;

import java.util.HashMap;
import java.util.Map;

import cc.icefire.market.api.BaseApiContext;
import cc.icefire.market.api.response.CategoryResponse;
import cc.icefire.market.model.AppOrGame;

/**
 * 评论列表请求接口，
 * 方法：POST 
 * 路径：/api/categories
 * 参数： type=0
 */
public class CategoryRequest extends BaseApiRequest<CategoryResponse> {

	private static final String path = "api/categories";
	
	/**
	 * 分类类型，游戏分类还是应用分类
	 */
	private final AppOrGame type;
	
	public CategoryRequest(AppOrGame type) {
		this.type = type;
	}
	
	public AppOrGame getType() {
		return type;
	}

	@Override
	public Map<String, String> getTextParams(BaseApiContext context) {
		HashMap<String, String> businessParams = new HashMap<String, String>();
		businessParams.put("type", type.ordinal() + "");
		return businessParams;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Class<CategoryResponse> getResponseClass() {
		return CategoryResponse.class;
	}

	@Override
	public String getCacheRelativePathOrURL() {
		return makeCachePath("api", "categoris", type == AppOrGame.APP ? "app" : "game");
	}

	/**
	 * cache lasts for 6 hours
	 */
	@Override
	public long getCacheTime() {
		return 60 * 60 * 6;
	}

}
