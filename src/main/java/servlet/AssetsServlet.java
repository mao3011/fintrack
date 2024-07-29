package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AssetsController;
import exception.FintrackException;
import model.Asset;
import util.ErrorHandler;
import util.LoggingUtil;
import util.SecurityUtils;
import util.ValidationUtils;

@WebServlet("/assets/*")
public class AssetsServlet extends HttpServlet {
    private static final Logger logger = LoggingUtil.getLogger(AssetsServlet.class);
    private AssetsController assetsController;

    @Override
    public void init() throws ServletException {
        super.init();
        assetsController = new AssetsController();
        logger.info("AssetsServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!SecurityUtils.isUserLoggedIn(request)) {
                logger.warning("Unauthorized access attempt");
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "User not logged in");
            }

            String action = SecurityUtils.getActionFromPath(request);
            logger.info("Processing GET request with action: " + action);

            switch (action) {
                case "":
                case "list":
                    listAssets(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    logger.warning("Invalid action requested: " + action);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error in doGet: " + e.getMessage());
            ErrorHandler.handleException(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!SecurityUtils.isUserLoggedIn(request)) {
                logger.warning("Unauthorized access attempt");
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "User not logged in");
            }

            if (!SecurityUtils.isValidCSRFToken(request)) {
                logger.warning("Invalid CSRF token");
                throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "Invalid CSRF token");
            }

            String action = SecurityUtils.getActionFromPath(request);
            logger.info("Processing POST request with action: " + action);

            switch (action) {
                case "add":
                    addAsset(request, response);
                    break;
                case "update":
                    updateAsset(request, response);
                    break;
                case "delete":
                    deleteAsset(request, response);
                    break;
                default:
                    logger.warning("Invalid action requested: " + action);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.severe("Error in doPost: " + e.getMessage());
            ErrorHandler.handleException(request, response, e);
        }
    }

    private void listAssets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = SecurityUtils.getUserIdFromSession(request);
        logger.info("Listing assets for user: " + userId);

        List<Asset> assets = assetsController.getAssetsByUserId(userId);
        BigDecimal totalAssets = assetsController.getTotalAssetsByUserId(userId);
        BigDecimal totalLiabilities = assetsController.getTotalLiabilitiesByUserId(userId);
        BigDecimal netWorth = assetsController.getNetWorth(userId);

        request.setAttribute("assets", assets);
        request.setAttribute("totalAssets", totalAssets);
        request.setAttribute("totalLiabilities", totalLiabilities);
        request.setAttribute("netWorth", netWorth);
        request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));

        logger.info("Assets listed successfully for user: " + userId);
        request.getRequestDispatcher("/WEB-INF/jsp/assets/list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String assetIdStr = request.getParameter("id");
        logger.info("Showing edit form for asset: " + assetIdStr);

        if (!ValidationUtils.isPositiveInteger(assetIdStr)) {
            logger.warning("Invalid asset ID: " + assetIdStr);
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid asset ID");
        }

        int assetId = Integer.parseInt(assetIdStr);
        Asset asset = assetsController.getAssetById(assetId);

        if (asset == null) {
            logger.warning("Asset not found: " + assetId);
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Asset not found");
        }

        int userId = SecurityUtils.getUserIdFromSession(request);
        if (asset.getUserId() != userId) {
            logger.warning("Unauthorized access attempt to edit asset: " + assetId + " by user: " + userId);
            throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "You don't have permission to edit this asset");
        }

        request.setAttribute("asset", asset);
        request.setAttribute("csrfToken", SecurityUtils.generateCSRFToken(request));

        logger.info("Edit form displayed for asset: " + assetId);
        request.getRequestDispatcher("/WEB-INF/jsp/assets/edit.jsp").forward(request, response);
    }

    private void addAsset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = SecurityUtils.getUserIdFromSession(request);
        String name = SecurityUtils.sanitizeHtml(request.getParameter("name"));
        String type = request.getParameter("type");
        String balanceStr = request.getParameter("balance");

        logger.info("Attempting to add asset for user: " + userId);

        if (!ValidationUtils.isValidAssetName(name) || !ValidationUtils.isValidAssetType(type) || !ValidationUtils.isValidMoney(balanceStr)) {
            logger.warning("Invalid input data for adding asset");
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid input data");
        }

        BigDecimal balance = new BigDecimal(balanceStr);

        boolean success = assetsController.addAsset(userId, name, type, balance);

        if (success) {
            logger.info("Asset added successfully for user: " + userId);
            response.sendRedirect(request.getContextPath() + "/assets");
        } else {
            logger.severe("Failed to add asset for user: " + userId);
            throw new FintrackException(FintrackException.ErrorCode.DATABASE_ERROR, "Failed to add asset");
        }
    }

    private void updateAsset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = SecurityUtils.getUserIdFromSession(request);
        String assetIdStr = request.getParameter("id");
        String name = SecurityUtils.sanitizeHtml(request.getParameter("name"));
        String type = request.getParameter("type");
        String balanceStr = request.getParameter("balance");

        logger.info("Attempting to update asset: " + assetIdStr + " for user: " + userId);

        if (!ValidationUtils.isPositiveInteger(assetIdStr) || !ValidationUtils.isValidAssetName(name)
                || !ValidationUtils.isValidAssetType(type) || !ValidationUtils.isValidMoney(balanceStr)) {
            logger.warning("Invalid input data for updating asset");
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid input data");
        }

        int assetId = Integer.parseInt(assetIdStr);
        BigDecimal balance = new BigDecimal(balanceStr);

        Asset asset = assetsController.getAssetById(assetId);
        if (asset == null || asset.getUserId() != userId) {
            logger.warning("Unauthorized access attempt to update asset: " + assetId + " by user: " + userId);
            throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "You don't have permission to update this asset");
        }

        asset.setName(name);
        asset.setType(type);
        asset.setBalance(balance);

        boolean success = assetsController.updateAsset(asset);

        if (success) {
            logger.info("Asset updated successfully: " + assetId);
            response.sendRedirect(request.getContextPath() + "/assets");
        } else {
            logger.severe("Failed to update asset: " + assetId);
            throw new FintrackException(FintrackException.ErrorCode.DATABASE_ERROR, "Failed to update asset");
        }
    }

    private void deleteAsset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = SecurityUtils.getUserIdFromSession(request);
        String assetIdStr = request.getParameter("id");

        logger.info("Attempting to delete asset: " + assetIdStr + " for user: " + userId);

        if (!ValidationUtils.isPositiveInteger(assetIdStr)) {
            logger.warning("Invalid asset ID: " + assetIdStr);
            throw new FintrackException(FintrackException.ErrorCode.INVALID_INPUT, "Invalid asset ID");
        }

        int assetId = Integer.parseInt(assetIdStr);

        Asset asset = assetsController.getAssetById(assetId);
        if (asset == null || asset.getUserId() != userId) {
            logger.warning("Unauthorized access attempt to delete asset: " + assetId + " by user: " + userId);
            throw new FintrackException(FintrackException.ErrorCode.AUTHENTICATION_FAILED, "You don't have permission to delete this asset");
        }

        boolean success = assetsController.deleteAsset(assetId);

        if (success) {
            logger.info("Asset deleted successfully: " + assetId);
            response.sendRedirect(request.getContextPath() + "/assets");
        } else {
            logger.severe("Failed to delete asset: " + assetId);
            throw new FintrackException(FintrackException.ErrorCode.DATABASE_ERROR, "Failed to delete asset");
        }
    }
}