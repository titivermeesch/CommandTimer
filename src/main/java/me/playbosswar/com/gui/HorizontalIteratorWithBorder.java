package me.playbosswar.com.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import fr.minuskube.inv.content.SlotPos;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HorizontalIteratorWithBorder implements SlotIterator {
    private final InventoryContents contents;
    private final SmartInventory inv;

    private boolean started = false;
    private boolean allowOverride = true;
    private int row;
    private int column;
    private final Set<SlotPos> blacklisted = new HashSet<>();

    public HorizontalIteratorWithBorder(Player player, InventoryContents contents, SmartInventory inv,
                                        int itemsPerPage, int rowLine, int row, int column) {
        this.contents = contents;
        this.inv = inv;
        this.row = row;
        this.column = column;
        Pagination pagination = contents.pagination();

        pagination.setItemsPerPage(itemsPerPage);
        pagination.addToIterator(this);

        ItemStack backItem = Items.generateItem(LanguageKey.BACK_LABEL, XMaterial.ARROW);
        ItemStack nextItem = Items.generateItem(LanguageKey.NEXT_LABEL, XMaterial.ARROW);
        contents.set(rowLine, 3, ClickableItem.of(backItem, e -> inv.open(player, pagination.previous().getPage())));
        contents.set(rowLine, 5, ClickableItem.of(nextItem, e -> inv.open(player, pagination.next().getPage())));
    }

    public HorizontalIteratorWithBorder(Player player, InventoryContents contents, SmartInventory inv) {
        this.contents = contents;
        this.inv = inv;
        this.row = 1;
        this.column = 1;
        Pagination pagination = contents.pagination();

        pagination.setItemsPerPage(28);
        pagination.addToIterator(this);

        ItemStack backItem = Items.generateItem(LanguageKey.BACK_LABEL, XMaterial.ARROW);
        ItemStack nextItem = Items.generateItem(LanguageKey.NEXT_LABEL, XMaterial.ARROW);
        contents.set(5, 3, ClickableItem.of(backItem, e -> inv.open(player, pagination.previous().getPage())));
        contents.set(5, 5, ClickableItem.of(nextItem, e -> inv.open(player, pagination.next().getPage())));
    }

    @Override
    public Optional<ClickableItem> get() {
        return contents.get(row, column);
    }

    @Override
    public SlotIterator set(ClickableItem item) {
        if(canPlace()) {
            contents.set(row, column, item);
        }

        return this;
    }

    @Override
    public SlotIterator previous() {
        if(row == 0 && column == 0) {
            this.started = true;
            return this;
        }

        do {
            if(!this.started) {
                this.started = true;
            } else {
                if(--column == 0) {
                    column = inv.getColumns() - 2;
                    row--;
                }
            }
        }
        while(!canPlace() && (row != 0 || column != 0));

        return this;
    }

    @Override
    public SlotIterator next() {
        if(ended()) {
            this.started = true;
            return this;
        }

        do {
            if(!this.started) {
                this.started = true;
            } else {
                column = ++column % (inv.getColumns() - 1);

                if(column == 0) {
                    column++;
                    row++;
                }
            }
        }
        while(!canPlace() && !ended());

        return this;
    }

    @Override
    public SlotIterator blacklist(int row, int column) {
        this.blacklisted.add(SlotPos.of(row, column));
        return this;
    }

    @Override
    public SlotIterator blacklist(SlotPos slotPos) {
        return blacklist(slotPos.getRow(), slotPos.getColumn());
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public SlotIterator row(int row) {
        this.row = row;
        return this;
    }

    @Override
    public int column() {
        return column;
    }

    @Override
    public SlotIterator column(int column) {
        this.column = column;
        return this;
    }

    @Override
    public boolean started() {
        return this.started;
    }

    @Override
    public boolean ended() {
        return row == inv.getRows() - 1
                && column == inv.getColumns() - 1;
    }

    @Override
    public boolean doesAllowOverride() {
        return allowOverride;
    }

    @Override
    public SlotIterator allowOverride(boolean override) {
        this.allowOverride = override;
        return this;
    }

    private boolean canPlace() {
        return !blacklisted.contains(SlotPos.of(row, column)) && (allowOverride || !this.get().isPresent());
    }
}
