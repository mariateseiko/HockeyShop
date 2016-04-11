package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.util.ActionResult;
import by.bsuir.hockeyshop.command.util.AllowedPage;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.service.ItemService;
import by.bsuir.hockeyshop.service.impl.ItemServiceImpl;
import by.bsuir.hockeyshop.command.util.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
/**
 * Class {@code NewItemCommand} is an admin-only implementation of {@see ActionCommand}
 * for adding new items to the catalog
 */
public class NewItemCommand implements ActionCommand {
    private static final String PARAM_NAME = "itemname";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_COLOR = "color";
    private static final String PARAM_PRICE = "price";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_STATUS = "status";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String ATTR_ERROR = "errorMessage";
    private static final String COMMAND_VIEW_PAGE = "/controller?command=view_page&page=";
    private static final String PARAM_FILE = "image_file";
    private static final String PARAM_ADDITIONAL = "additional";
    private static final String PARAM_DESCRIPTION = "description";

    private static ItemService itemService = ItemServiceImpl.getInstance();

    /**
     * Handles request to the servlet by adding a new item to the catalog
     *
     * @param request request from the servlet, containing new item description
     * @return path to the new item page in case of success
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        Item item = new Item();
        item.setName(request.getParameter(PARAM_NAME));
        item.setSize(request.getParameter(PARAM_SIZE));
        item.setSize(request.getParameter(PARAM_SIZE));
        item.setColor(request.getParameter(PARAM_COLOR));
        String price = request.getParameter(PARAM_PRICE);
        item.setType(ItemType.valueOf(request.getParameter(PARAM_TYPE).toUpperCase()));
        item.setStatus(ItemStatus.valueOf(request.getParameter(PARAM_STATUS).toUpperCase()));
        item.setAdditionalInfo(request.getParameter(PARAM_ADDITIONAL));
        item.setDescription(request.getParameter(PARAM_DESCRIPTION));
        try {
            Part part = request.getPart(PARAM_FILE);
            if (Validator.validateItem(item, price, part)) {
                item.setPrice(Integer.parseInt(price));
                if (part != null) {
                    item.setImagePath(uploadImage(request));
                }
                String resultAttr = ATTR_ERROR;
                if (itemService.addItem(item)) {
                    resultAttr = ATTR_SUCCESS;
                }
                request.getSession().setAttribute(resultAttr, ActionResult.ITEM_ADDED_TO_CATALOG);
            } else {
                request.getSession().setAttribute(ATTR_ERROR, ActionResult.VALIDATION_FAILED);
            }
            page = COMMAND_VIEW_PAGE + AllowedPage.NEW_ITEM;
        } catch (ServletException | IOException e) {
            throw new CommandException("Failed to process image file part", e);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }

    private String uploadImage(HttpServletRequest request) throws IOException, ServletException {
        String path = request.getServletContext().getRealPath(
                ConfigurationManager.getProperty("path.items"));
        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";
        String fullPath = path + File.separator + fileName;
        File file = new File(fullPath);
        file.createNewFile();
        try (InputStream inputStream = request.getPart(PARAM_FILE).getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        return fileName;
    }
}
