package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "usage_purchase_allocations")
public class UsagePurchaseAllocation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "custom-id")
    @org.hibernate.annotations.GenericGenerator(name = "custom-id", strategy = "util.CustomIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = "prefix", value = "UPA"),
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_usage_alloc_id")
        }
    )
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usage_id", nullable = false)
    private MaterialUsage usage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id", nullable = false)
    private MaterialPurchase purchase;

    @Column(name = "allocated_quantity", nullable = false)
    private BigDecimal allocatedQuantity;

    public UsagePurchaseAllocation() {
    }

    public UsagePurchaseAllocation(MaterialUsage usage, MaterialPurchase purchase, BigDecimal allocatedQuantity) {
        this.usage = usage;
        this.purchase = purchase;
        this.allocatedQuantity = allocatedQuantity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public MaterialUsage getUsage() { return usage; }
    public void setUsage(MaterialUsage usage) { this.usage = usage; }
    public MaterialPurchase getPurchase() { return purchase; }
    public void setPurchase(MaterialPurchase purchase) { this.purchase = purchase; }
    public BigDecimal getAllocatedQuantity() { return allocatedQuantity; }
    public void setAllocatedQuantity(BigDecimal allocatedQuantity) { this.allocatedQuantity = allocatedQuantity; }
}
