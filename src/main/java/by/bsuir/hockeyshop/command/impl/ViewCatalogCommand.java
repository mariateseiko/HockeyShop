package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemType;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.ItemService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.ItemServiceImpl;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
/**
 * Class {@code ViewCatalogCommand} is an all-users implementation of {@see ActionCommand}
 * for retrieving items from the catalog
 */
public class ViewCatalogCommand implements ActionCommand {
    private static final ItemService ITEM_SERVICE = ItemServiceImpl.getInstance();
    static final int MAX_ITEMS_PER_PAGE = 8;
    static final String PARAM_TYPE = "type";
    static final String PARAM_NO_ITEMS_MESSAGE = "noItemsMessage";
    static final String PARAM_ERROR_MESSAGE = "errorMessage";
    static final int START_PAGE_NUMBER = 1;
    static final String PARAM_PAGE = "page";
    static final String ATTR_ITEMS = "items";
    static final String ATTR_NUM_PAGES = "numPages";
    static final String ATTR_CURRENT_PAGE = "currentPage";
    static final String PARAM_DIRECTION = "dir";
    static final String DESC_DIRECTION = "desc";
    static final String ASC_DIRECTION = "asc";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    /**
     * Handles request to the servlet by retrieving and returning a list of items of specified type for a specified page
     * and sorting direction. Max list length is 8. If direction is not specified, ascending sorting is applied. Default
     * page number is 1.
     * @param request request from the servlet, containing an item's type, sorting direction and a start page.
     * @return path to the catalog's page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        int totalNumberOfItems, pageNumber, totalNumberOfPages;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            ItemType type = ItemType.valueOf(request.getParameter(PARAM_TYPE).toUpperCase());
            pageNumber = definePageNumber(request);
            totalNumberOfItems = ITEM_SERVICE.selectItemsCountByType(type);
            totalNumberOfPages = defineTotalNumberOfPages(totalNumberOfItems);
            if (totalNumberOfItems > 0 && pageNumber <= totalNumberOfPages) {
                String direction = defineDirection(request);
                List<Item> items = selectItems(type, direction, pageNumber);
                if (items.size() > 0) {
                    request.setAttribute(ATTR_ITEMS, items);
                    request.setAttribute(ATTR_NUM_PAGES, totalNumberOfPages);
                    request.setAttribute(ATTR_CURRENT_PAGE, pageNumber);
                    request.setAttribute(PARAM_DIRECTION, direction);
                } else {
                    request.setAttribute(PARAM_NO_ITEMS_MESSAGE,
                            messageManager.getProperty("message.catalog.noitems"));
                }
            } else {
                request.setAttribute(PARAM_NO_ITEMS_MESSAGE,
                        messageManager.getProperty("message.catalog.noitems"));
            }
            request.setAttribute(PARAM_TYPE, type.toString().toLowerCase());
            page = ConfigurationManager.getProperty("path.page.catalog");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }

    private int definePageNumber(HttpServletRequest request) {
        String pageNumberParam;
        int pageNumber;
        if ((pageNumberParam = request.getParameter(PARAM_PAGE)) != null) {
            pageNumber = Integer.parseInt(pageNumberParam);
        } else {
            pageNumber = START_PAGE_NUMBER;
        }
        return pageNumber;
    }

    private String defineDirection(HttpServletRequest request) {
        String directionParam;
        if ((directionParam = request.getParameter(PARAM_DIRECTION)) != null) {
            if (!(ASC_DIRECTION.equals(directionParam) || DESC_DIRECTION.equals(directionParam))) {
                directionParam = ASC_DIRECTION;
            }
        } else {
            directionParam = ASC_DIRECTION;
        }
        return directionParam;
    }

    private int defineTotalNumberOfPages(int totalNumberOfItems) {
        return (int)Math.ceil((double)totalNumberOfItems/MAX_ITEMS_PER_PAGE);
    }

    private List<Item> selectItems(ItemType type, String direction, int pageNumber) throws ServiceException {
        List<Item> items;
        int offset = (pageNumber - 1) * MAX_ITEMS_PER_PAGE;
        if (DESC_DIRECTION.equals(direction)) {
            items = ITEM_SERVICE.selectItemsByTypeSortedByPriceDesc(type, offset, MAX_ITEMS_PER_PAGE);
        } else {
            items = ITEM_SERVICE.selectItemsByTypeSortedByPriceAsc(type, offset, MAX_ITEMS_PER_PAGE);
        }
        return items;
    }
}
