package demo.wheel.kankan.ecommerce_heady.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constants {

    public static final int HI_REQUEST_CODE_FILTER = 1234;

    public enum rankingEnum {
        Viewed("Most Viewed Products"),
        Ordered("Most OrdeRed Products"),
        Shared("Most ShaRed Products");

        final String rankingType;

        private static final List<rankingEnum> PRODUCTSLIST;

        static {
            PRODUCTSLIST = new ArrayList<>();
            Collections.addAll(PRODUCTSLIST, rankingEnum.values());
        }

        rankingEnum(final String name) {
            this.rankingType = name;
        }

        public String getName() {
            return rankingType;
        }

        public static List<rankingEnum> getClientList() {
            return PRODUCTSLIST;
        }

        @Override
        public String toString() {
            return rankingType;
        }
    }
}
