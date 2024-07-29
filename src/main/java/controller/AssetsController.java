package controller;

import java.math.BigDecimal;
import java.util.List;

import dao.AssetDAO;
import model.Asset;

public class AssetsController {
    private AssetDAO assetDAO;

    public AssetsController() {
        this.assetDAO = new AssetDAO();
    }

    public boolean addAsset(int userId, String name, String type, BigDecimal balance) {
        Asset newAsset = new Asset(userId, name, type, balance);
        return assetDAO.insertAsset(newAsset);
    }

    public Asset getAssetById(int id) {
        return assetDAO.getAssetById(id);
    }

    public List<Asset> getAssetsByUserId(int userId) {
        return assetDAO.getAssetsByUserId(userId);
    }

    public List<Asset> getAssetsByUserIdAndType(int userId, String type) {
        return assetDAO.getAssetsByUserIdAndType(userId, type);
    }

    public boolean updateAsset(Asset asset) {
        return assetDAO.updateAsset(asset);
    }

    public boolean deleteAsset(int id) {
        return assetDAO.deleteAsset(id);
    }

    public BigDecimal getTotalAssetsByUserId(int userId) {
        return assetDAO.getTotalAssetsByUserId(userId);
    }

    public BigDecimal getTotalLiabilitiesByUserId(int userId) {
        return assetDAO.getTotalLiabilitiesByUserId(userId);
    }

    public BigDecimal getNetWorth(int userId) {
        BigDecimal totalAssets = getTotalAssetsByUserId(userId);
        BigDecimal totalLiabilities = getTotalLiabilitiesByUserId(userId);
        return totalAssets.subtract(totalLiabilities);
    }
}