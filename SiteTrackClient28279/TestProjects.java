import config.RMIConnection;
import service.interfaces.MaterialStockService;
import dto.ProjectMaterialStockDTO;
import java.util.List;

public class TestProjects {
    public static void main(String[] args) throws Exception {
        MaterialStockService service = RMIConnection.getInstance().getService(MaterialStockService.class);
        System.out.println("Stock for PRJ-001:");
        List<ProjectMaterialStockDTO> stock = service.getStockByProject("PRJ-001");
        for (ProjectMaterialStockDTO s : stock) {
            System.out.println(s.getMaterialName() + " - Qty: " + s.getQuantityAvailable());
        }
        System.exit(0);
    }
}
