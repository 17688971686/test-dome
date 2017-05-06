package cs.auto.core.config;

/**
 * Description: 包信息配置类
 * User: tzg
 * Date: 2017/5/6 17:59
 */
public class PackageConfig {

    public PackageConfig() {
    }

    public PackageConfig(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "cs";

    /**
     * 父包模块名。
     */
    private String moduleName = null;

    private String domain = "domain";

    private String dto = "model";

    private String repository = "repository.repositoryImpl";

    private String repositoryImpl = "repository.repositoryImpl";

    private String service = "service";

    private String serviceImpl = "service";

    private String controller = "controller";

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getRepositoryImpl() {
        return repositoryImpl;
    }

    public void setRepositoryImpl(String repositoryImpl) {
        this.repositoryImpl = repositoryImpl;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

}