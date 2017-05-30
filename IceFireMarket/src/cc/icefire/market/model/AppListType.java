package cc.icefire.market.model;

/**
 * 应用列表类型，将来可根据需要扩展
 */
public enum AppListType {

	SEARCH, //搜索结果
	CATEGORY, //分类目录下应用
	POPULAR, //最近流行
	SELECTED, //编辑精选
	NEW_RELEASES, //最新上架
	TOP_CHARTS, //下载排行榜
	RECOMMENDEDED; //为你推荐
	
	public static AppListType fromInt(int value) {
		switch(value) {
        case 0:
            return SEARCH;
        case 1:
            return CATEGORY;
        case 2:
        	return POPULAR;
        case 3:
        	return SELECTED;
        case 4:
        	return NEW_RELEASES;
        case 5:
        	return TOP_CHARTS;
        case 6:
        	return RECOMMENDEDED;
        }
        return null;
	}
	
}
