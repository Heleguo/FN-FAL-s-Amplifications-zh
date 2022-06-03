package ne.fnfal113.fnamplifications.utils.pdc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.annotation.Nonnull;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemStackListPdcType<T, Z> implements PersistentDataType<byte[], List<ItemStack>> {

    private final Class<T> primitive;
    private final Class<Z> complex;

    public static final PersistentDataType<byte[], List<ItemStack>> ITEM_STACK_LIST_PDC = new ItemStackListPdcType<>(byte[].class, List.class);

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return (Class<byte[]>) primitive;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public Class<List<ItemStack>> getComplexType() {
        return (Class<List<ItemStack>>) complex;
    }

    @Override
    @Nonnull
    public byte[] toPrimitive(@Nonnull List<ItemStack> complex, @Nonnull PersistentDataAdapterContext context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try(BukkitObjectOutputStream inputStream = new BukkitObjectOutputStream(new BufferedOutputStream(bytes))){
            inputStream.writeObject(complex);
        } catch (IOException | SecurityException | NullPointerException e){
            e.printStackTrace();
        }
        return bytes.toByteArray();
    }

    @SneakyThrows
    @Override
    @Nonnull
    public List<ItemStack> fromPrimitive(@Nonnull byte[] primitive, @Nonnull PersistentDataAdapterContext context) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(primitive);
        try(BukkitObjectInputStream bytes = new BukkitObjectInputStream(new BufferedInputStream(inputStream))){
            return (List<ItemStack>) bytes.readObject();
        } catch (IOException | ClassCastException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
