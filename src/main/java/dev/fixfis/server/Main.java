package dev.fixfis.server;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import dev.fixfis.server.entities.DesperdicioDto;
import dev.fixfis.server.entities.LugarDto;
import dev.fixfis.server.entities.TipoResiduoDto;
import dev.fixfis.server.entities.security.CredencialDto;
import dev.fixfis.server.entities.security.UsuarioDto;
import dev.fixfis.server.request.AddDesperdicioTandaRequest;
import dev.fixfis.server.request.AdminRequierd;
import dev.fixfis.server.request.TandasRequest;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class Main {

    static void main() {
        Metrics.userUUID = UUID.fromString("6f24e7c7-4e2a-41ce-8941-065c5c1e469f");

        Object array = ApiClient.create("/tandas/getTableTanda",Long.class)
                .postter(11L).result().getData();

        Type type = TypeToken
                .getParameterized(List.class, DesperdicioDto.class)
                .getType();

        List<DesperdicioDto> desperdicioDtoList = new Gson().fromJson(
                array.toString(),type
        );
        desperdicioDtoList.forEach(System.out::println);
    }

    private static void getTAbla() {
        Object array = ApiClient.create("/tandas/getTableTanda", Long.class)
                .postter(4L).result().getData();

        Type type = TypeToken
                .getParameterized(List.class, DesperdicioDto.class)
                .getType();

        List<DesperdicioDto> desperdicioDtoList = new Gson().fromJson(
                array.toString(), type
        );
        desperdicioDtoList.forEach(System.out::println);
    }

    private static void getTipoResiduo() {
        List<UsuarioDto> list = ApiClient.create("/crerol/users").toList(UsuarioDto.class);
        list.forEach(System.out::println);
//        Type t = new TypeToken<List<TipoResiduoDto>>() {
//        }.getType();
//
//        List<TipoResiduoDto> l = new Gson().fromJson(
//                ApiClient.create("/residuos/residuos").getter(JsonArray.class),
//                t
//        );
//        l.forEach(System.out::println);
        // TODO agregar una forma para poder crear lugares desde este mismo panel
    }

    private static void getLugares() {
        Type t = new TypeToken<List<LugarDto>>() {
        }.getType();

        List<LugarDto> l = new Gson().fromJson(
                ApiClient.create("/lugares/").getter(JsonArray.class),
                t
        );
        l.forEach(System.out::println);
    }

    private static void getUsers() {
        Type type = new TypeToken<List<UsuarioDto>>() {
        }.getType();

        List<UsuarioDto> users = new Gson().fromJson(
                ApiClient.create("/crerol/users").getter(JsonArray.class),
                type
        );
        users.forEach(u -> System.out.println(u.getApellido()));
    }

    private static void crearTandaV2() {
        LugarDto lugarDto = new LugarDto(
                3L,
                "BCP",
                "calle benavides",
                "al constado de un casino, cerca de la plaza de armas"
        );
        AdminRequierd<LugarDto> dto = new AdminRequierd<>();
        dto.setData(lugarDto);
        System.out.println(ApiClient.create("/tandas/crearTanda", AdminRequierd.class)
                .postter(dto).devResult());
    }

    private static void addDespercioTanda() {
        AddDesperdicioTandaRequest addTandaRequest = new AddDesperdicioTandaRequest();
        addTandaRequest.setIdTanda(1L);
        addTandaRequest.setDesperdicio(new DesperdicioDto(null, "Juan Carlos", 12, 1L));
        System.out.println(ApiClient.create("/tandas/addDesperdicio", AddDesperdicioTandaRequest.class)
                .postter(addTandaRequest).devResult());
    }

    private static void crearTanda() {
        TandasRequest tanda = new TandasRequest();
        tanda.setLugar(3L);

        System.out.println(ApiClient.create("/tandas/crearTanda", TandasRequest.class)
                .postter(tanda).devResult());
    }

    private static void crearDesperdicio() {
        AdminRequierd<DesperdicioDto> dto = new AdminRequierd<>();
        dto.setData(
                new DesperdicioDto(
                        null,
                        "Juan Carlos",
                        12,
                        1L
                )
        );
        System.out.println(ApiClient.create("/residuos/desperdicios", AdminRequierd.class)
                .postter(dto).devResult());
    }

    private static void crearTipoResiduos() {
        AdminRequierd<TipoResiduoDto> dto = new AdminRequierd<>();
        dto.setData(new TipoResiduoDto(
                null,
                "Latas de aluminio",
                "Reciclable",
                null,
                null)
        );
        System.out.println(ApiClient.create("/residuos/residuos", AdminRequierd.class)
                .postter(dto).devResult());
    }

    private static void eliminarDatos() {
        List<Long> l = List.of(
                4L
        );
        System.out.println(ApiClient.create("/lugares/", List.class)
                .deleter(l).devResult());
    }

    private static void crearLugar() {
        AdminRequierd<LugarDto> dto = new AdminRequierd<>();
        dto.setData(
                new LugarDto(
                        null,
                        "BBVA",
                        "calle benavides",
                        "al constado de un casino, cerca de la plaza de armas"
                )
        );

        System.out.println(ApiClient.create("/lugares/", AdminRequierd.class)
                .postter(dto).devResult());
    }

    private static void createUser() {
        AdminRequierd<UsuarioDto> c = new AdminRequierd<>();
        c.setData(
                new UsuarioDto(
                        null,
                        "jhon Edward",
                        "Francia Minaya",
                        "1111111",
                        true,
                        new CredencialDto(
                                null,
                                "jFrancia",
                                "12345"
                        )
                )
        );

        System.out.println(ApiClient.create("/crerol/users", AdminRequierd.class)
                .postter(c).devResult());
    }

//    private static void grantAdmin() {
//        GrantAdmin grantAdmin = new GrantAdmin();
//        grantAdmin.setUuid("6f24e7c7-4e2a-41ce-8941-065c5c1e469f");
//        System.out.println(ApiClient.create("/crerol/grantAdmin", GrantAdmin.class)
//                .postter(grantAdmin).devResult());
//    }


    /**
     * TRANSFORMAR JSON A CLASE
     * String json = "{\"id\":1, \"nombre\":\"Laptop\", \"precio\":2000, \"categoria\":{   \"id\":1,   \"nombre\":\"Tecnologia\" }}";
     *
     * Gson gson = new Gson();
     * Producto producto = gson.fromJson(json, Producto.class);
     *
     * System.out.println(producto.toString());
     */

    /**
     * TRANSFORMAR CLASE A JSON
     * Gson gson = new Gson();
     *
     * Producto producto = new Producto();
     * producto.setNombre("Producto 1");
     * producto.setPrecio(100.0);
     *
     * String json = gson.toJson(producto);
     *
     * System.out.println(json);
     */
}
