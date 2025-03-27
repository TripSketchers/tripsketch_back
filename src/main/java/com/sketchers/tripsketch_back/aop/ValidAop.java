package com.sketchers.tripsketch_back.aop;

import com.sketchers.tripsketch_back.exception.ValidException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashMap;
import java.util.Map;

// ✅ 공통적인 유효성 검사 처리 AOP 클래스
@Aspect  // 이 클래스가 AOP(Aspect-Oriented Programming) 기능을 수행하는 클래스임을 명시
@Component  // 스프링 빈으로 등록
public class ValidAop {

    // 🔸 1. Pointcut 설정
    // @ValidAop 어노테이션이 붙은 메서드를 대상으로 지정
    @Pointcut("@annotation(com.sketchers.tripsketch_back.aop.annotation.ValidAop)")
    private void pointCut() {}

    // 🔸 2. 메서드 실행 전/후를 감싸서 처리하는 Advice
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        BeanPropertyBindingResult bindingResult = null;

        // 🔍 메서드 매개변수들 중 BindingResult가 있는지 찾기
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if (arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }

        // 🟢 BindingResult가 없다면 그냥 메서드 실행
        if (bindingResult == null) {
            return proceedingJoinPoint.proceed();
        }

        // 🔴 에러가 있다면 map에 담아서 ValidException 발생시킴
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
            throw new ValidException(errorMap);  // 커스텀 예외 던지기
        }

        // ✅ 에러가 없다면 원래 메서드 실행
        return proceedingJoinPoint.proceed();
    }
}
