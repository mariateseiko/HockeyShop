package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.service.ItemService;
import by.bsuir.hockeyshop.service.impl.ItemServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
    static final String PARAM_NAME = "name";
    static final String PARAM_SIZE = "size";
    static final String PARAM_COLOR = "color";
    static final String PARAM_PRICE = "price";
    static final String PARAM_TYPE = "type";
    static final String PARAM_STATUS = "status";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String COMMAND_VIEW_PAGE = "/controller?command=view_page&page=";
    static final String PARAM_FILE = "image_file";
    static final String PARAM_ADDITIONAL = "additional";
    static final String PARAM_DESCRIPTION = "description";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final ItemService ITEM_SERVICE = ItemServiceImpl.getInstance();

    /**
     * Handles request to the servlet by adding a new item to the catalog
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
        item.setPrice(Integer.parseInt(request.getParameter(PARAM_PRICE)));
        item.setType(ItemType.valueOf(request.getParameter(PARAM_TYPE).toUpperCase()));
        item.setStatus(ItemStatus.valueOf(request.getParameter(PARAM_STATUS).toUpperCase()));
        item.setAdditionalInfo(request.getParameter(PARAM_ADDITIONAL));
        item.setDescription(request.getParameter(PARAM_DESCRIPTION));
        try {
            if(request.getParts().size() > 0) {
                item.setImagePath(uploadImage(request));
            }
        } catch (ServletException|IOException|CommandException e){
            throw new CommandException(e);
        }

        try {
            String resultAttr = ATTR_ERROR;
            if (ITEM_SERVICE.addItem(item)) {
               resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.ITEM_ADDED_TO_CATALOG);
            page = COMMAND_VIEW_PAGE + ConfigurationManager.getProperty("path.page.admin.newitem");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }

    private String uploadImage(HttpServletRequest request) throws CommandException {
        String path = request.getServletContext().getRealPath(
                ConfigurationManager.getProperty("path.items"));

        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";

        String fullPath = path + File.separator + fileName;
        File file = new File(fullPath);
        try {
            file.createNewFile();
        } catch(IOException e) {
            throw new CommandException("Creation of a new file failed", e);
        }

        try (InputStream inputStream = request.getPart(PARAM_FILE).getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException|ServletException e) {
            throw new CommandException("File upload to server failed", e);
        }
        return fileName;
    }
}
