package meow.illina.hexssence.api;

import meow.illina.hexssence.Hexssence;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@SuppressWarnings("SameParameterValue")
public final class HexssenceTags {
    public static final class Items {
        public static final TagKey<Item> VALID_ESSENCE = create("essence_jar/valid");

        private static TagKey<Item> create(String name) {
            return TagKey.create(Registries.ITEM, Hexssence.id(name));
        }
    }
}
