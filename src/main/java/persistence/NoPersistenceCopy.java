package persistence;


import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Atributos anotados com essa annotation, serão ignorados no processo de criação da copia do contexto persistente.
 * @author Victor Lindberg (victor.silva@serpro.gov.br)
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface NoPersistenceCopy {
	
	/**
	 * Classes que são especializações da classe onde o atributo que esta anotado com esta anotação está
	 * que esta anotação de "não copia" será válida. Exemplo. Nas classes A e B que estendem de C mas que
	 * se deseja que um atributo chamado 'cpf' na classe C não seja levado em consideração no processo de copia
	 * na subclasse B. Então para isto basta anotar o atributo 'cpf' na classe C definindo o atributo 'forClasses'
	 * passando como valor as classes A e B.
	 */
	Class<?>[] forClasses() default Object.class;
	
	/**
	 * Se true então o campo será setado para null no objeto copia gerado a partir de um outro.
	 * Se false, então o valor do objeto original será setado no objeto copia. O valor default é 'false'.
	 */
	boolean setNull() default false;

}
